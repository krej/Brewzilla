package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.graphics.Color;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashSetupController;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeViewController;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Responses.RecipeStatsResponse;
import beer.unaccpetable.brewzilla.Repositories.IRepository;
import beer.unaccpetable.brewzilla.Tools.Calculations;

public class RecipeEditorController extends BaseLogic<RecipeEditorController.View> {

    private @NotNull RecipeViewController m_ViewController;
    private @NotNull MashSetupController m_MashSetupController;

    private IRepository m_repo;

    private boolean m_bDeleted = false;

    RecipeEditorController(IRepository repository) {
        m_repo = repository;
        m_ViewController = new RecipeViewController(repository);
        m_MashSetupController = new MashSetupController(repository);

        CreateRecipeViewControllerListeners();
        CreateMashSetupControllerListeners();
    }

    private void CreateMashSetupControllerListeners() {
        m_MashSetupController.setRecipeRequestListener(m_ViewController::getRecipe);

        m_MashSetupController.addGristRatioChangedEventListener((v) -> {
            Recipe r = m_ViewController.getRecipe();
            r.recipeParameters.gristRatio = v;
            SaveRecipe();
        });

        m_MashSetupController.addInitialMashTempChangedListener((value -> {
            Recipe r = m_ViewController.getRecipe();
            r.recipeParameters.initialMashTemp = value;
            SaveRecipe();
        }));

        m_MashSetupController.addTargetMashTempChangedListener((value -> {
            Recipe r = m_ViewController.getRecipe();
            r.recipeParameters.targetMashTemp = value;
            SaveRecipe();
        }));

        m_MashSetupController.addShowMessageEventListener(view::ShowToast);
    }

    private void CreateRecipeViewControllerListeners() {
        m_ViewController.addSaveRecipeEventListener(this::SaveRecipe);
        m_ViewController.addShowMessageEventListener((s) -> view.ShowToast(s));
        m_ViewController.addStatsChangedEventListener(this::PopulateStats);

        m_ViewController.addErrorOccurredListener((bRecipeModifiedElsewhere -> {
            if (bRecipeModifiedElsewhere) {
                m_ViewController.setReadOnly(true);
            }
        }));

    }

    void LoadRecipe(String sID) {
        if (sID != null && sID.length() > 0) {
            m_repo.LoadRecipe(sID, new RepositoryCallback() {
                @Override
                public void onSuccess(String t) {
                    Recipe r = Tools.convertJsonResponseToObject(t, Recipe.class);

                    view.SetTitle(r.name);
                    m_ViewController.SetRecipe(r);
                    m_MashSetupController.PopulateParameters(r.recipeParameters);
                }

                @Override
                public void onError(VolleyError error) {
                    view.ShowToast(Tools.ParseVolleyError(error));
                }
            });
        } else {
            //I don't think this is possible anymore, but just in case... To be removed later...
            view.SetTitle("Create Recipe");
        }
    }

    private void PopulateStats(RecipeStatistics recipeStats) {
        m_MashSetupController.PopulateMashStats(recipeStats);


        int iColor = Color.parseColor(Calculations.GetSRMColor((int)recipeStats.srm));
        Color cDark = Color.valueOf(iColor);
        float fDark = 0.8f;
        int iDarkColor = Color.rgb(cDark.red() * fDark, cDark.green() * fDark, cDark.blue() * fDark);
        view.SetTitleSRMColor(iColor, iDarkColor);
    }

    void SaveRecipe() {
        SaveRecipe(m_ViewController.getRecipe());
    }

    private void SaveRecipe(Recipe recipe) {
        if (m_bDeleted) return;

        m_repo.SaveRecipe(recipe.idString, recipe, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                RecipeStatsResponse response = Tools.convertJsonResponseToObject(t, RecipeStatsResponse.class);
                if (response.Success) {
                    //comment out this line to test locking
                    recipe.lastModifiedGuid = response.lastModifiedGuid;
                    PopulateStats(response.recipeStats);
                    recipe.recipeStats = response.recipeStats;
                    m_ViewController.saveComplete();

                    //This should always be last so it gets the real copy of the data
                    //m_OriginalData = CurrentRecipe.BuildRestData();
                }else {
                    HandleRecipeStatsResponseError(response);
                }
            }

            @Override
            public void onError(VolleyError error) {
                view.ShowToast("ERROR");
            }
        });
    }

    private void HandleRecipeStatsResponseError(RecipeStatsResponse response) {
        if (response.Message.contains("Recipe has been modified")) {

            view.ShowToast("Recipe has been modified elsewhere. Please refresh.");

            m_ViewController.setReadOnly(true);

        } else {
            view.ShowToast(response.Message);
        }
    }


    void DeleteRecipe() {
        m_repo.DeleteRecipe(m_ViewController.getRecipe().idString, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                view.FinishActivity(m_ViewController.getRecipe().idString, 0, true, "");
                m_bDeleted = true;
            }

            @Override
            public void onError(VolleyError error) {
                view.ShowToast("Error deleting recipe.");
            }
        });
    }

    void AskDeleteRecipe() {
        view.PromptDeletion();
    }


    void GoBack() {

        view.FinishActivity(m_ViewController.getRecipe().idString, m_ViewController.getRecipe().recipeStats.abv, false, m_ViewController.getRecipe().style.name);
    }

    RecipeViewController getRecipeViewController() {
        return m_ViewController;
    }

    MashSetupController getMashViewController() {
        return m_MashSetupController;
    }

    void showChangeStylePrompt() {
        m_ViewController.showStylePrompt();
    }

    public interface View {
        void FinishActivity(String sIDString, double dAbv, boolean bDeleted, String sStyleName);
        void ShowToast(String sMessage);
        void SetTitle(String sTitle);
        void PromptDeletion();
        void SetTitleSRMColor(int iColor, int iDarkColor);
    }

}
