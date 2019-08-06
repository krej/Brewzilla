package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.content.Intent;
import android.graphics.Color;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashSetupController;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeViewController;
import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Responses.LastModifiedGuidResponse;
import beer.unaccpetable.brewzilla.Models.Responses.RecipeStatsResponse;
import beer.unaccpetable.brewzilla.Repositories.IRepository;
import beer.unaccpetable.brewzilla.Tools.Calculations;

public class RecipeEditorController extends BaseLogic<RecipeEditorController.View> {

    private @NotNull RecipeViewController m_ViewController;

    private IRepository m_repo;

    private boolean m_bDeleted = false;

    RecipeEditorController(IRepository repository) {
        m_repo = repository;
        m_ViewController = new RecipeViewController(repository);

        CreateRecipeViewControllerListeners();
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
        //m_MashSetupController.PopulateMashStats(recipeStats);


        int iColor = Color.parseColor(Calculations.GetSRMColor((int)recipeStats.srm));
        Color cDark = Color.valueOf(iColor);
        float fDark = 0.8f;
        int iDarkColor = Color.rgb(cDark.red() * fDark, cDark.green() * fDark, cDark.blue() * fDark);
        view.SetTitleSRMColor(iColor, iColor);
    }

    void SaveRecipe() {
        SaveRecipe(m_ViewController.getRecipe());
    }

    private void SaveRecipe(Recipe recipe) {
        SaveRecipe(recipe, null);
    }

    private void SaveRecipe(Recipe recipe, RepositoryCallback callback) {
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
                    if (callback != null)
                        callback.onSuccess("");
                }else {
                    HandleRecipeStatsResponseError(response);
                }
            }

            @Override
            public void onError(VolleyError error) {
                view.ShowToast("ERROR");
                if (callback != null)
                    callback.onError(error);
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

    void showChangeStylePrompt() {
        m_ViewController.showStylePrompt();
    }

    public void startNewBrewLog() {
        Recipe recipe = m_ViewController.getRecipe();
        SaveRecipe(recipe, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                try {
                    BrewLog brewLog = new BrewLog();
                    brewLog.name = Tools.FormatDate(new Date(), "MM/dd/yyyy");
                    brewLog.originalRecipe = m_ViewController.getRecipe().clone();
                    brewLog.rectifiedRecipe = m_ViewController.getRecipe().clone();
                    brewLog.recipeIdString = brewLog.originalRecipe.idString;

                    m_repo.SaveBrewLog(brewLog, new RepositoryCallback() {
                        @Override
                        public void onSuccess(String t) {
                            LastModifiedGuidResponse response = Tools.convertJsonResponseToObject(t, LastModifiedGuidResponse.class);
                            if (response.Success)
                                view.launchBrewLogActivity(response.idString);

                            //TODO: Add this new brewlog to the brew log list in the recipe editor screen
                        }

                        @Override
                        public void onError(VolleyError error) {
                            view.ShowToast("Failed to save brew log.");
                        }
                    });

                } catch (Exception e) {
                    view.ShowToast("Failed to create brew log.");
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    public void LoadBrewLogs(String sID) {
        m_repo.LoadBrewLogsForRecipe(sID, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                BrewLog[] brewLogs = Tools.convertJsonResponseToObject(t, BrewLog[].class);
                view.PopulateBrewLogList(brewLogs);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public interface View {
        void FinishActivity(String sIDString, double dAbv, boolean bDeleted, String sStyleName);
        void ShowToast(String sMessage);
        void SetTitle(String sTitle);
        void PromptDeletion();
        void SetTitleSRMColor(int iColor, int iDarkColor);
        void launchBrewLogActivity(String idString);
        void PopulateBrewLogList(BrewLog[] brewLogs);
    }

}
