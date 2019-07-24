package beer.unaccpetable.brewzilla.Repositories;

import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;

import beer.unaccpetable.brewzilla.Models.Recipe;

public interface IRepository {
    void LoadRecipeList(RepositoryCallback callback);
    void LoadRecipe(String sRecipeID, RepositoryCallback callback);
    void LoadRecipeWithAllIngredients(String sRecipeID, RepositoryCallback callback);
    void SaveRecipe(String sRecipeID, Recipe r, RepositoryCallback callback);
    void MashInfusionCalculation(String sRecipeID, String T1, String T2, String Wm, String Tw, RepositoryCallback callback);
    void LoadStyles(RepositoryCallback callback);
}
