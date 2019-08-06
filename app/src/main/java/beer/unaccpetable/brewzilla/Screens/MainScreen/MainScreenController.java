package beer.unaccpetable.brewzilla.Screens.MainScreen;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.Responses.RecipeStatsResponse;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class MainScreenController extends BaseLogic<MainScreenController.View> {

    private IRepository m_repo;

    public MainScreenController(IRepository repo) {
        m_repo = repo;
    }

    public void LoadRecipes(boolean bStopRefresh) {

        m_repo.LoadRecipeList(new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                Recipe[] r = Tools.convertJsonResponseToObject(t, Recipe[].class);
                view.PopulateRecipeList(r);
                if (bStopRefresh)
                    view.StopRefresh();
            }

            @Override
            public void onError(VolleyError error) {
                view.ShowToast(Tools.ParseVolleyError(error));
                if (bStopRefresh)
                    view.StopRefresh();
            }
        });

    }

    public void CreateNewRecipe(String sName, Style sStyle) {
        final Recipe r = new Recipe();
        r.name = sName;
        r.style = sStyle;
        r.recipeStats.Initialize();
        r.recipeParameters.Initialize();

        m_repo.SaveRecipe("", r, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                RecipeStatsResponse response = Tools.convertJsonResponseToObject(t, RecipeStatsResponse.class);
                r.idString = response.idString;

                view.AddRecipe(r);
                view.OpenRecipe(r.idString);
            }

            @Override
            public void onError(VolleyError error) {

                view.ShowToast(Tools.ParseVolleyError(error));
            }
        });

    }

    public void ShowNewRecipeDialog() {
        m_repo.LoadStyles(new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                Style[] styles = Tools.convertJsonResponseToObject(t, Style[].class);
                view.ShowNewRecipeDialog(styles);
            }

            @Override
            public void onError(VolleyError error) {
                view.ShowToast(Tools.ParseVolleyError(error));
            }
        });
    }

    public interface View {
        void PopulateRecipeList(Recipe[] r);
        void ShowToast(String sMessage);
        void OpenRecipe(String sIDString);
        void ShowNewRecipeDialog(Style[] styles);
        void StopRefresh();

        void AddRecipe(Recipe r);
    }
}
