package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class RecipeEditorController extends BaseLogic<RecipeEditorController.View> {

    public Recipe CurrentRecipe;

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

                    view.SetTitle(CurrentRecipe.name);
                    view.PopulateStats(CurrentRecipe.recipeStats);
                    view.PopulateHops(CurrentRecipe.hops);
                    view.PopulateYeasts(CurrentRecipe.yeasts);
                    view.PopulateFermentables(CurrentRecipe.fermentables);

                    view.PopulateHopDialog(r.Hops);
                    view.PopulateFermentableDialog(r.Fermentables);
                    view.PopulateYeastDialog(r.Yeasts);
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

    public void RecipeUpdated() {
        view.GetIngredients();
        CurrentRecipe.recipeStats.Initialize();
        CurrentRecipe.Save();
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
    }
}
