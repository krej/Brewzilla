package beer.unaccpetable.brewzilla.Screens.MainScreen;

import android.widget.Toast;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.Responses.RecipeStatsResponse;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class MainScreenController extends BaseLogic<MainScreenController.View> {

    private IRepository m_repo;

    public MainScreenController(IRepository repo) {
        m_repo = repo;
    }

    public void LoadRecipes() {

        m_repo.LoadRecipeList(new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                Recipe[] r = Tools.convertJsonResponseToObject(t, Recipe[].class);
                view.PopulateRecipeList(r);
            }

            @Override
            public void onError(VolleyError error) {
                view.ShowToast(Tools.ParseVolleyError(error));
            }
        });

    }

    public void CreateNewRecipe(String sName, String sStyle) {
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

                view.OpenRecipe(r.idString);
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
    }
}
