package beer.unaccpetable.brewzilla.Repositories;

import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;

public interface IRepository {
    void LoadRecipeList(RepositoryCallback callback);
    void LoadRecipe(String sRecipeID, RepositoryCallback callback);
}
