package beer.unaccpetable.brewzilla.Screens.IngredientManager;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Adjunct;
import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class IngredientManagerController<T> extends BaseLogic<IngredientManagerController.View> {

    private IRepository m_repo;

    public IngredientManagerController(IRepository repo) {
        m_repo = repo;
    }

    public void LoadIngredientList(String sCollectionName) {
        m_repo.LoadIngredientList(sCollectionName, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                SendListToScreen(t, sCollectionName);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void SendListToScreen(String t, String sCollectionName) {
        ListableObject[] data;

        data = ConvertJsonToArray(t, sCollectionName);

        view.PopulateList(data);
    }

    private @NotNull ListableObject[] ConvertJsonToArray(String t, String sCollectionName) {
        switch (sCollectionName) {
            case "hop":
                return Tools.convertJsonResponseToObject(t, Hop[].class);
            case "fermentable":
                return Tools.convertJsonResponseToObject(t, Fermentable[].class);
            case "yeast":
                return Tools.convertJsonResponseToObject(t, Yeast[].class);
            case "adjunct":
                return Tools.convertJsonResponseToObject(t, Adjunct[].class);
        }

        return new ListableObject[0];
    }


    public interface View {
        void PopulateList(ListableObject[] data);
    }
}
