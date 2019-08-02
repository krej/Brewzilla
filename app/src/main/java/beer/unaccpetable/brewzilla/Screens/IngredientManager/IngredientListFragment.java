package beer.unaccpetable.brewzilla.Screens.IngredientManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unacceptable.unacceptablelibrary.Adapters.IAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;

public class IngredientListFragment<T> extends Fragment
implements IngredientManagerController.View
{

    private RecyclerView m_RecyclerView;
    private NewAdapter m_Adapter;
    private IAdapterViewControl m_ViewControl;

    private int m_iDialogLayout;

    private IngredientManagerController m_Controller;
    private String m_sIngredientCollection;

    private ListableObject[] m_data;

    public IngredientListFragment() {
        m_Controller = new IngredientManagerController<T>(new Repository());
        m_Controller.attachView(this);
    }

    public static <T> IngredientListFragment newInstance() {
        IngredientListFragment fragment = new IngredientListFragment<T>();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setDialogLayout(int iDialogLayout) {
        m_iDialogLayout = iDialogLayout;
    }

    public void setAdapterViewControl(IAdapterViewControl vc) {
        m_ViewControl = vc;
    }

    public void setIngredientCollection(String sCollection) {
        m_sIngredientCollection = sCollection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredient_manager, container, false);

        m_RecyclerView = rootView.findViewById(R.id.ingredientList);

        m_Adapter = Tools.setupRecyclerView(m_RecyclerView, getContext(), R.layout.one_line_list, m_iDialogLayout, false, m_ViewControl, true, true);

        //TODO: Whats happening is when you go to adjuncts and then back to fermentables, its recreating the view and this method is getting called again.
        //TODO: I'm not 100% sure but I'm wondering if the adapter is staying populated (since its not displayed) and all I need to do is reattach it to the recycler view, rather than call ALL of setupRecyclerView and repopulate the data
        /*if (m_data != null && m_data.length > 0)
            PopulateList(m_data);*/


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        m_Controller.LoadIngredientList(m_sIngredientCollection);
    }

    public void PopulateList(ListableObject[] data) {
        for (ListableObject l : data) {
            m_Adapter.add(l);
        }
    }

    public void addNewItem() {
        m_Adapter.showAddItemDialog(getContext(), null);
    }
}
