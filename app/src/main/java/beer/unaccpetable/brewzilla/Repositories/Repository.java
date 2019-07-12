package beer.unaccpetable.brewzilla.Repositories;

import com.android.volley.Request;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Preferences;

import beer.unaccpetable.brewzilla.Models.Recipe;

public class Repository implements IRepository {
    @Override
    public void LoadRecipeList(RepositoryCallback callback) {
        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/recipe", null, callback, true, false);
    }

    @Override
    public void LoadRecipe(String sRecipeID, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/recipe/" + sRecipeID, null, callback, true, false);
    }

    @Override
    public void SaveRecipe(String sRecipeID, Recipe r, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.POST, Preferences.BeerNetAPIURL() + "/recipe/" + sRecipeID, r.BuildRestData(), callback, true, false);
    }
}
