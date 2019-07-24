package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.support.design.widget.TabLayout;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Models.Response;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeParameters;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Responses.RecipeStatsResponse;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class RecipeEditorController extends BaseLogic<RecipeEditorController.View> {

    public Recipe CurrentRecipe;
    private boolean bPopulatingScreen;

    private IRepository m_repo;

    public RecipeEditorController(IRepository repository) {
        m_repo = repository;
    }

    public void LoadRecipe(String sID) {
        if (sID != null && sID.length() > 0) {
            m_repo.LoadRecipeWithAllIngredients(sID, new RepositoryCallback() {
                @Override
                public void onSuccess(String t) {
                    RecipeEditorViewModel r = Tools.convertJsonResponseToObject(t, RecipeEditorViewModel.class);
                    CurrentRecipe = r.Recipe;
                    FixBadData(); //TODO: Temp, only for now when some old data doesn't exist

                    bPopulatingScreen = true;
                    view.PopulateStyleDropDown(r.Styles);

                    view.SetTitle(CurrentRecipe.name);
                    if (CurrentRecipe.beerStyle != null)
                        view.SetStyle(r.Styles, CurrentRecipe.beerStyle);
                    view.PopulateStats(CurrentRecipe.recipeStats);
                    view.PopulateHops(CurrentRecipe.hops);
                    view.PopulateYeasts(CurrentRecipe.yeasts);
                    view.PopulateFermentables(CurrentRecipe.fermentables);
                    view.PopulateParameters(CurrentRecipe.recipeParameters);

                    view.PopulateHopDialog(r.Hops);
                    view.PopulateFermentableDialog(r.Fermentables);
                    view.PopulateYeastDialog(r.Yeasts);
                    bPopulatingScreen = false;
                }

                @Override
                public void onError(VolleyError error) {
                    view.ShowToast(Tools.ParseVolleyError(error));
                }
            });
        } else {
            //TODO: Is this even possible anymore? I changed it so it saves the recipe before going to the screen then loads it.
            view.SetTitle("Create Recipe");
        }
    }

    private void FixBadData() {
        if (CurrentRecipe.recipeParameters == null)
            CurrentRecipe.recipeParameters = new RecipeParameters();

    }

    public void RecipeUpdated() {
        if (bPopulatingScreen) return;

        view.GetIngredients();

        m_repo.SaveRecipe(CurrentRecipe.idString, CurrentRecipe, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                RecipeStatsResponse response = Tools.convertJsonResponseToObject(t, RecipeStatsResponse.class);
                view.PopulateStats(response.recipeStats);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void SetHops(ArrayList<ListableObject> dataset) {
        CurrentRecipe.hops.clear();
        for (ListableObject i : dataset) {
            CurrentRecipe.hops.add((HopAddition)i);
        }
    }

    public void SetFermentables(ArrayList<ListableObject> dataset) {
        CurrentRecipe.fermentables.clear();
        for (ListableObject i : dataset) {
            CurrentRecipe.fermentables.add((FermentableAddition) i);
        }
    }

    public void SetYeasts(ArrayList<ListableObject> dataset) {
        CurrentRecipe.yeasts.clear();
        for (ListableObject i : dataset) {
            CurrentRecipe.yeasts.add((Yeast)i);
        }
    }

    public void TabSelected(TabLayout.Tab tab) {
        if (tab.getText().equals("RECIPE")) {
            view.SwitchToRecipeView();
        } else {
            view.SwitchToMashView();
        }
    }

    public void setGristRatio(double dValue) {
        CurrentRecipe.recipeParameters.gristRatio = dValue;
        RecipeUpdated();
    }

    public void SetInitialMashTemp(double dTemp) {
        CurrentRecipe.recipeParameters.initialMashTemp = dTemp;
        RecipeUpdated();
    }

    public void SetTargetMashTemp(double dTemp) {
        CurrentRecipe.recipeParameters.targetMashTemp = dTemp;
        RecipeUpdated();
    }

    public void CalcMashInfusion(String sCurrentTemp, String sTargetMashTemp, String sTotalWaterInMash, String sHLTTemp) {
        if (bPopulatingScreen) return;

        /*double dCurrentTemp = Tools.ParseDouble(sCurrentTemp);
        double dTargetMashTemp = Tools.ParseDouble(sTargetMashTemp);
        double dTotalWaterInMash = Tools.ParseDouble(sTotalWaterInMash);
        double dHLTTemp = Tools.ParseDouble(sHLTTemp);*/

        m_repo.MashInfusionCalculation(CurrentRecipe.idString, sCurrentTemp, sTargetMashTemp, sTotalWaterInMash, sHLTTemp, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                view.MashInfusionShowWaterToAdd(t);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void SetStyle(Style style) {
        CurrentRecipe.beerStyle = style;
        RecipeUpdated();
    }

    public interface View {
        void ShowToast(String sMessage);
        void SetTitle(String sTitle);
        void PopulateStats(RecipeStatistics recipeStats);
        void PopulateHops(ArrayList<HopAddition> hops);
        void PopulateYeasts(ArrayList<Yeast> yeasts);
        void PopulateFermentables(ArrayList<FermentableAddition> fermentables);

        void PopulateYeastDialog(ArrayList<Yeast> yeasts);

        void PopulateHopDialog(ArrayList<Hop> hops);

        void PopulateFermentableDialog(ArrayList<Fermentable> fermentables);
        void GetIngredients();
        void SwitchToMashView();
        void SwitchToRecipeView();

        void PopulateParameters(RecipeParameters recipeParameters);

        void MashInfusionShowWaterToAdd(String t);

        void PopulateStyleDropDown(Style[] styles);
        void SetStyle(Style[] styles, Style beerStyle);
    }
}
