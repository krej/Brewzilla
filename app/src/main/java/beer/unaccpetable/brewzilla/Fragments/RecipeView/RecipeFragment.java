package beer.unaccpetable.brewzilla.Fragments.RecipeView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Adapters.SearchDialogAdapterViewControl;
import beer.unaccpetable.brewzilla.Models.AdjunctAddition;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.HopAddition;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Models.YeastAddition;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditorController;

public class RecipeFragment
        extends Fragment
        implements RecipeViewController.View
{
    RecipeViewController m_Controller;

    RecyclerView lstGrains, lstHops, lstYeasts, lstAdjuncts;
    NewAdapter m_HopAdapter, m_YeastAdapter, m_FermentableAdapter, m_AdjunctAdapter;
    HopAdditionAdapterViewControl m_vcHop;
    FermentableAdditionAdapterViewControl m_vcFermentable;
    YeastAdditionAdapterViewControl m_vcYeasts;
    AdjunctAdditionAdapterViewControl m_vcAdjunct;

    TextView m_tvStyle;

    Button m_btnAddFermentable, m_btnAddHop, m_btnAddYeast, m_btnAddAdjunct;
    NestedScrollView m_recipeScrollView;
    ViewPager m_vpStats;
    TabLayout m_tbStats;

    StatsSectionsPagerAdapter m_statsSectionsPagerAdapter;

    public static RecipeFragment newInstance() {
        RecipeFragment fragmentFirst = new RecipeFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setController(RecipeViewController controller) {
        m_Controller = controller;
        m_Controller.attachView(this);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_editor, container, false);
        m_statsSectionsPagerAdapter = new StatsSectionsPagerAdapter(getActivity().getSupportFragmentManager());


        FindUIElements(view);
        SetupLists();
        SetupAddButtonListeners();

        m_vpStats.setAdapter(m_statsSectionsPagerAdapter);
        m_tbStats.setupWithViewPager(m_vpStats);

        //8.31.2020. Uhh.. I'm not sure what this is. It's old and I'm just checking in my latest changes...
        m_Controller.afterOnCreateView();
        //m_Controller.activityCreated();
        return view;
    }

    private void FindUIElements(View view){
        m_vpStats = view.findViewById(R.id.statsPager);
        m_tbStats = view.findViewById(R.id.recipeStatsTabDots);
        m_recipeScrollView = view.findViewById(R.id.recipeScrollView);
        lstGrains = view.findViewById(R.id.listGrains);
        lstHops = view.findViewById(R.id.listHops);
        lstYeasts = view.findViewById(R.id.listYeast);
        lstAdjuncts = view.findViewById(R.id.listAdjunct);
        m_btnAddFermentable = view.findViewById(R.id.btnAddFermentable);
        m_btnAddHop = view.findViewById(R.id.btnAddHop);
        m_btnAddYeast = view.findViewById(R.id.btnAddYeast);
        m_btnAddAdjunct = view.findViewById(R.id.btnAddAdjunct);
        m_tvStyle = view.findViewById(R.id.recipeStyle);
    }


    private void SetupAddButtonListeners() {
        m_btnAddFermentable.setOnClickListener((v) -> m_Controller.ShowAddDialog(Recipe.IngredientType.Fermntable));
        m_btnAddHop.setOnClickListener((v) -> m_Controller.ShowAddDialog(Recipe.IngredientType.Hop));
        m_btnAddYeast.setOnClickListener((v) -> m_Controller.ShowAddDialog(Recipe.IngredientType.Yeast));
        m_btnAddAdjunct.setOnClickListener((v) -> m_Controller.ShowAddDialog(Recipe.IngredientType.Adjunct));
    }

    @Override
    public void PopulateHops(ArrayList<HopAddition> hops) {
        Tools.PopulateAdapter(m_HopAdapter, hops);
    }

    @Override
    public void PopulateYeasts(ArrayList<YeastAddition> yeasts) {
        Tools.PopulateAdapter(m_YeastAdapter, yeasts);
    }

    @Override
    public void PopulateFermentables(ArrayList<FermentableAddition> fermentables) {
        Tools.PopulateAdapter(m_FermentableAdapter, fermentables);
    }

    @Override
    public void PopulateAdjuncts(ArrayList<AdjunctAddition> adjuncts) {
        Tools.PopulateAdapter(m_AdjunctAdapter, adjuncts);
    }

    private void SetupLists() {
        m_vcHop = new HopAdditionAdapterViewControl(m_Controller);
        m_vcFermentable = new FermentableAdditionAdapterViewControl(m_Controller);
        m_vcYeasts = new YeastAdditionAdapterViewControl();
        m_vcAdjunct = new AdjunctAdditionAdapterViewControl(m_Controller);

        m_HopAdapter = Tools.setupRecyclerView(lstHops, getContext(), R.layout.list_hop_addition, R.layout.fragment_hop_dialog, false, m_vcHop, true, true, true);
        m_HopAdapter.setSwipeFlags(ItemTouchHelper.END);
        m_YeastAdapter = Tools.setupRecyclerView(lstYeasts, getContext(), R.layout.list_yeast_addition, R.layout.fragment_yeast_dialog, false, m_vcYeasts, true, true, true);
        m_YeastAdapter.setSwipeFlags(ItemTouchHelper.END);
        m_FermentableAdapter = Tools.setupRecyclerView(lstGrains, getContext(), R.layout.list_fermentable_addition, R.layout.fragment_malt_dialog, false, m_vcFermentable, true, true, true);
        m_FermentableAdapter.setSwipeFlags(ItemTouchHelper.END);
        m_AdjunctAdapter = Tools.setupRecyclerView(lstAdjuncts, getContext(), R.layout.list_adjunct_addition, R.layout.fragment_adjunct_dialog, false,m_vcAdjunct, true, true, true);
        m_AdjunctAdapter.setSwipeFlags(ItemTouchHelper.END);

        m_HopAdapter.setNotifySwipeDelete(createNotifySwipeDeleteAdapter(m_HopAdapter, Recipe.IngredientType.Hop));
        m_FermentableAdapter.setNotifySwipeDelete(createNotifySwipeDeleteAdapter(m_FermentableAdapter, Recipe.IngredientType.Fermntable));
        m_YeastAdapter.setNotifySwipeDelete(createNotifySwipeDeleteAdapter(m_YeastAdapter, Recipe.IngredientType.Yeast));
        m_AdjunctAdapter.setNotifySwipeDelete(createNotifySwipeDeleteAdapter(m_AdjunctAdapter, Recipe.IngredientType.Adjunct));
    }

    private NewAdapter.INotifySwipeDelete createNotifySwipeDeleteAdapter(NewAdapter adapter, Recipe.IngredientType ingredientType) {
        NewAdapter.INotifySwipeDelete nsd = new NewAdapter.INotifySwipeDelete() {

            @Override
            public void notifyDelete(int position, ListableObject i) {
                Snackbar mySnackbar = Snackbar.make(m_recipeScrollView, R.string.item_deleted, Snackbar.LENGTH_SHORT);

                mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //adapter.add(position, i);
                        m_Controller.AddIngredient(i, ingredientType, position);
                    }
                });
                mySnackbar.show();

                m_Controller.DeleteIngredient(i, ingredientType);
            }
        };

        return nsd;
    }

    @Override
    public void PopulateStats(RecipeStatistics stats) {

        m_statsSectionsPagerAdapter.PopulateStats(stats);

    }

    @Override
    public void AddFermentable(FermentableAddition fa) {
        m_FermentableAdapter.add(fa);
    }

    @Override
    public void AddHop(HopAddition ha) {
        m_HopAdapter.add(ha);
    }

    @Override
    public void AddYeast(YeastAddition ya) {
        m_YeastAdapter.add(ya);
    }

    @Override
    public void AddAdjunct(AdjunctAddition aa) {
        m_AdjunctAdapter.add(aa);
    }

    @Override
    public void SetStyle(Style beerStyle) {
        Tools.SetText(m_tvStyle, beerStyle.name);
    }

    @Override
    public void SetStyleRanges(Style style) {
        m_statsSectionsPagerAdapter.PopulateStyleRanges(style);

    }

    @Override
    public void SetScreenReadOnly(boolean bReadOnly) {

        //m_spStyle.setEnabled(bEnabled);
        m_btnAddFermentable.setEnabled(!bReadOnly);
        m_btnAddHop.setEnabled(!bReadOnly);
        m_btnAddYeast.setEnabled(!bReadOnly);
        m_btnAddAdjunct.setEnabled(!bReadOnly);
        /*m_txtInitialMashTemp.setEnabled(bEnabled);
        m_spGristRatio.setEnabled(bEnabled);
        m_txtTargetMashTemp.setEnabled(bEnabled);*/

        m_HopAdapter.setReadOnly(bReadOnly, lstHops);
        m_YeastAdapter.setReadOnly(bReadOnly, lstYeasts);
        m_FermentableAdapter.setReadOnly(bReadOnly, lstGrains);
        m_AdjunctAdapter.setReadOnly(bReadOnly, lstAdjuncts);

        Tools.hideKeyboard(getActivity());

    }

    @Override
    public void ShowAddDialog(ArrayList data, Recipe.IngredientType ingredientType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_search_list, null);
        RecyclerView rv = v.findViewById(R.id.rvSearchResults);
        SearchDialogAdapterViewControl svc = new SearchDialogAdapterViewControl();
        NewAdapter adapter = Tools.setupRecyclerView(rv, getContext(), R.layout.one_line_list, 0, false, svc, true);

        Tools.PopulateAdapter(adapter, data);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        EditText et = v.findViewById(R.id.searchBox);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        svc.attachAdapter(adapter);
        svc.setItemSelectedListener((i) -> {m_Controller.AddIngredient(i, ingredientType); dialog.dismiss();});

        dialog.show();
        et.requestFocus();
    }

    public RecipeViewController getController() {
        return m_Controller;
    }

   /* private void SetupSwipeRefresh(final String sIDString) {
        m_SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_Controller.LoadRecipe(sIDString);
            }
        });
    }*/

   @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
       super.onActivityCreated(savedInstanceBundle);

       //m_Controller.activityCreated();
   }

   @Override
    public void RefreshStatsLayout() {
       m_vpStats.requestLayout();
   }
}
