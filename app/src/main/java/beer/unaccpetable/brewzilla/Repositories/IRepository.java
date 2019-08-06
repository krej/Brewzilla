package beer.unaccpetable.brewzilla.Repositories;

import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;

import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.Models.Recipe;

public interface IRepository {
    void LoadRecipeList(RepositoryCallback callback);
    void LoadRecipe(String sRecipeID, RepositoryCallback callback);
    void LoadRecipeWithAllIngredients(String sRecipeID, RepositoryCallback callback);
    void SaveRecipe(String sRecipeID, Recipe r, RepositoryCallback callback);
    void MashInfusionCalculation(Recipe recipe, String T1, String T2, String Wm, String Tw, RepositoryCallback callback);
    void LoadStyles(RepositoryCallback callback);
    void DeleteRecipe(String sIDString, RepositoryCallback callback);
    void CalculateRecipeStats(Recipe r, RepositoryCallback callback);
    void LoadCollection(String sCollectionName, RepositoryCallback callback);
    void SaveBrewLog(BrewLog brewLog, RepositoryCallback callback);
    void LoadBrewLog(String idString, RepositoryCallback callback);
    void LoadBrewLogsForRecipe(String idString, RepositoryCallback callback);
}
