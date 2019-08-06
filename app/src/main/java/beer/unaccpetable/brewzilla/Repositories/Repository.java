package beer.unaccpetable.brewzilla.Repositories;

import com.android.volley.Request;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Preferences;

import beer.unaccpetable.brewzilla.Models.BrewLog;
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
    public void LoadRecipeWithAllIngredients(String sRecipeID, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/recipe/getWithIngredients/" + sRecipeID, null, callback, true, true);
    }

    @Override
    public void SaveRecipe(String sRecipeID, Recipe r, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.POST, Preferences.BeerNetAPIURL() + "/recipe/" + sRecipeID, r.BuildRestData(), callback, true, false);
    }

    @Override
    public void MashInfusionCalculation(Recipe recipe, String T1, String T2, String Wm, String Tw, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.POST, Preferences.BeerNetAPIURL() + "/recipe/mashInfusion/" + T1 + "/" + T2 + "/" + Wm + "/" + Tw + "/", recipe.BuildRestData(), callback, true, true);
    }

    @Override
    public void LoadStyles(RepositoryCallback callback) {
        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/style/", null, callback, true, false);
    }

    @Override
    public void DeleteRecipe(String sIDString, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.DELETE, Preferences.BeerNetAPIURL() + "/recipe/" + sIDString, null, callback, true, false);
    }

    @Override
    public void CalculateRecipeStats(Recipe r, RepositoryCallback callback) {
        //this is a POST because volley doesn't send the body for GETs

        String sEndpoint = "/recipe/" + r.idString + "/false";

        Network.WebRequest(Request.Method.POST, Preferences.BeerNetAPIURL() + sEndpoint, r.BuildRestData(), callback, true);
    }

    @Override
    public void LoadCollection(String sCollectionName, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/" + sCollectionName, null, callback, true);
    }

    @Override
    public void SaveBrewLog(BrewLog brewLog, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.POST, Preferences.BeerNetAPIURL() + "/brewlog/" + brewLog.idString, brewLog.BuildRestData(), callback, true);
    }

    @Override
    public void LoadBrewLog(String idString, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/brewlog/" + idString, null, callback, true);
    }

    @Override
    public void LoadBrewLogsForRecipe(String idString, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/brewlog/recipe/" + idString, null, callback, true);
    }
}
