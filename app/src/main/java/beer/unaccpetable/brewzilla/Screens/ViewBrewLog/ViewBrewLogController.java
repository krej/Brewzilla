package beer.unaccpetable.brewzilla.Screens.ViewBrewLog;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.ITimeSource;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import beer.unaccpetable.brewzilla.Fragments.BrewStats.BrewStatsController;
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
    private @NotNull BrewStatsController m_BrewStatsController;

    private @NotNull IRepository m_repo;

    private BrewLog m_BrewLog;
    private byte[] m_OriginalData;

    private ITimeSource m_oTimeSource;

    public ViewBrewLogController(@NotNull IRepository repository, ViewBrewLogController.View view, ITimeSource oTimeSource) {
        m_repo = repository;
        attachView(view);

        m_oTimeSource = oTimeSource;

        m_oOriginalController = new RecipeViewController(repository);
        m_oRectifiedController = new RecipeViewController(repository);
        m_MashSetupController = new MashSetupController(repository);
        m_BrewStatsController = new BrewStatsController(m_oTimeSource);


        CreateMashSetupControllerListeners();
        CreateRectifiedRecipeListeners();
        CreateBrewStatsListeners();
    }

    private void CreateBrewStatsListeners() {
        /*m_BrewStatsController.addFGChangedListener((fg -> m_BrewLog.fg = fg));
        m_BrewStatsController.addOGChangedListener((og -> m_BrewLog.og = og));
        m_BrewStatsController.addMashStartTimeChangedListener((startTime -> m_BrewLog.mashStartTime = startTime));
        m_BrewStatsController.addMashEndTimeChangedListener((endTime -> m_BrewLog.mashEndTime = endTime));*/

        m_BrewStatsController.addPropertyChangedListener((this::PropertyChanged));
    }

    private void PropertyChanged(BrewLog.Properties eProperty, Object value) {
        switch (eProperty) {
            case Vaurloff:
                m_BrewLog.vaurloff = (boolean)value;
                break;
            case FG:
                m_BrewLog.fg = (double)value;
                break;
            case OG:
                m_BrewLog.og = (double)value;
                break;
            case MashEndTime:
                m_BrewLog.mashEndTime = (String)value;
                break;
            case MashStartTime:
                m_BrewLog.mashStartTime = (String)value;
        }
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

    void SaveBrewLog() {
        if (Arrays.equals(m_OriginalData, m_BrewLog.BuildRestData())) return;


        m_repo.SaveBrewLog(m_BrewLog, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                LastModifiedGuidResponse response = Tools.convertJsonResponseToObject(t, LastModifiedGuidResponse.class);
                if (response.Success) {
                    m_BrewLog.lastModifiedGuid = response.lastModifiedGuid;
                    m_oRectifiedController.saveComplete();
                    m_OriginalData = m_BrewLog.BuildRestData();
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

    BrewStatsController getBrewStatsController() {
        return m_BrewStatsController;
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
                //m_BrewStatsController.PopulateStats(m_BrewLog.fg, m_BrewLog.og, m_BrewLog.mashStartTime);
                m_BrewStatsController.PopulateStats(m_BrewLog);
                view.SetScreenTitle(m_BrewLog.rectifiedRecipe.name, m_BrewLog.name);

                m_OriginalData = m_BrewLog.BuildRestData();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void pageSelected(int i) {
        /*if (i == 0) {
            m_oRectifiedController.refreshLayout();
        }*/
        SaveBrewLog();
    }


    public interface View {
        void ShowToast(String sMessage);
        void SetScreenTitle(String sBeerName, String sBrewLogName);
    }
}
