package beer.unaccpetable.brewzilla.Screens.ViewBrewLog;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashSetupController;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeViewController;
import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Responses.LastModifiedGuidResponse;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class ViewBrewLogController extends BaseLogic<ViewBrewLogController.View> {
    private @NotNull RecipeViewController m_oOriginalController;
    private @NotNull RecipeViewController m_oRectifiedController;
    private @NotNull MashSetupController m_MashSetupController;

    private @NotNull IRepository m_repo;

    private BrewLog m_BrewLog;

    public ViewBrewLogController(@NotNull IRepository repository, ViewBrewLogController.View view) {
        m_repo = repository;
        attachView(view);

        m_oOriginalController = new RecipeViewController(repository);
        m_oRectifiedController = new RecipeViewController(repository);
        m_MashSetupController = new MashSetupController(repository);


        CreateMashSetupControllerListeners();
        CreateRectifiedRecipeListeners();
    }

    private void CreateRectifiedRecipeListeners() {
        m_oRectifiedController.addSaveRecipeEventListener(this::SaveBrewLog);
        m_oRectifiedController.addShowMessageEventListener((s) -> view.ShowToast(s));
        m_oRectifiedController.addStatsChangedEventListener(this::PopulateStats);

        m_oRectifiedController.addErrorOccurredListener((bRecipeModifiedElsewhere -> {
            if (bRecipeModifiedElsewhere) {
                setReadOnly(true);
            }
        }));
    }

    private void setReadOnly(boolean bReadOnly) {

        m_oRectifiedController.setReadOnly(bReadOnly);
        m_MashSetupController.setReadOnly(bReadOnly);
    }

    private void PopulateStats(RecipeStatistics statistics) {
        m_MashSetupController.PopulateMashStats(statistics);
    }

    private void SaveBrewLog(Recipe recipe) {
        m_BrewLog.rectifiedRecipe = recipe;
        SaveBrewLog();
    }

    private void SaveBrewLog() {
        m_repo.SaveBrewLog(m_BrewLog, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                LastModifiedGuidResponse response = Tools.convertJsonResponseToObject(t, LastModifiedGuidResponse.class);
                if (response.Success) {
                    m_BrewLog.lastModifiedGuid = response.lastModifiedGuid;
                    m_oRectifiedController.saveComplete();
                } else {
                    if (response.Message.contains("Recipe has been modified")) {
                        setReadOnly(true);
                        view.ShowToast("Recipe has been modified. Please refresh.");
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void CreateMashSetupControllerListeners() {
        m_MashSetupController.setRecipeRequestListener(m_oRectifiedController::getRecipe);

        m_MashSetupController.addGristRatioChangedEventListener((v) -> {
            m_BrewLog.rectifiedRecipe.recipeParameters.gristRatio = v;
            m_oRectifiedController.RecalculateStats();
            SaveBrewLog();
        });

        m_MashSetupController.addInitialMashTempChangedListener((value -> {
            m_BrewLog.rectifiedRecipe.recipeParameters.initialMashTemp = value;
            m_oRectifiedController.RecalculateStats();
            SaveBrewLog();
        }));

        m_MashSetupController.addTargetMashTempChangedListener((value -> {
            m_BrewLog.rectifiedRecipe.recipeParameters.targetMashTemp = value;
            m_oRectifiedController.RecalculateStats();
            SaveBrewLog();
        }));

        m_MashSetupController.addShowMessageEventListener(view::ShowToast);
    }

    RecipeViewController getOriginalController() {
        return m_oOriginalController;
    }

    RecipeViewController getRectifiedController() {
        return m_oRectifiedController;
    }

    MashSetupController getMashController() {
        return m_MashSetupController;
    }

    void LoadBrewLog(String idString) {
        if (Tools.IsEmptyString(idString)) {
            view.ShowToast("Error loading brew log.");
            return;
        }

        m_repo.LoadBrewLog(idString, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                m_BrewLog = Tools.convertJsonResponseToObject(t, BrewLog.class);

                m_oRectifiedController.SetRecipe(m_BrewLog.rectifiedRecipe);
                //m_oOriginalController.SetRecipe(brewLog.originalRecipe);
                m_MashSetupController.PopulateParameters(m_BrewLog.rectifiedRecipe.recipeParameters);
                view.SetScreenTitle(m_BrewLog.rectifiedRecipe.name, m_BrewLog.name);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


    public interface View {
        void ShowToast(String sMessage);
        void SetScreenTitle(String sBeerName, String sBrewLogName);
    }
}
