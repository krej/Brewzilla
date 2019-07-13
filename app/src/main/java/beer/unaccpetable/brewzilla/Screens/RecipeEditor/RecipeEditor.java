package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;
import beer.unaccpetable.brewzilla.Screens.BaseActivity;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

public class RecipeEditor extends BaseActivity implements RecipeEditorController.View {

    private RecipeEditorController m_Controller;

    RecyclerView lstGrains, lstHops,lstYeasts;
    NewAdapter m_HopAdapter, m_YeastAdapter, m_FermentableAdapter, m_AdjunctAdapter;
    HopAdditionAdapterViewControl m_vcHop;
    FermentableAdditionAdapterViewControl m_vcFermentable;
    YeastAdditionAdapterViewControl m_vcYeasts;
    //private RecyclerView.LayoutManager m_HopLayoutManager, m_YeastLayoutManager, m_MaltLayoutManager;
    //private HopAdditionAdapter m_HopAdditionAdapter = new HopAdditionAdapter(R.layout.hop_list, R.layout.fragment_hop_dialog);
    //private YeastAdditionAdapter m_YeastAdditionAdapter = new YeastAdditionAdapter(R.layout.yeast_list, R.layout.fragment_yeast_dialog);
    //private YeastAdapter m_YeastAdapter = new YeastAdapter(R.layout.yeast_list, R.layout.fragment_yeast_dialog);
    //private FermentableAdditionAdapter m_MaltAdapter = new FermentableAdditionAdapter(R.layout.hop_list, R.layout.fragment_malt_dialog);

    private Boolean bShowExtraFab = false;
    FloatingActionButton fabGrain,fabHop, fabYeast;

    private TextView txtIBU, txtOG, txtFG, txtABV, txtSRM;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FindUIElements();

        fabHop.setImageResource(R.drawable.ic_hop);
        fabGrain.setImageResource(R.drawable.ic_grain);
        fabYeast.setImageResource(R.drawable.ic_test_tube);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SetExtraFABHideEvents();

        SetButtonClickEvents();

        m_Controller = new RecipeEditorController(new Repository());
        m_Controller.attachView(this);

        SetupLists();

        String sID = getIntent().getStringExtra("RecipeID");

        /*

        RefreshStats();
*/

        m_Controller.LoadRecipe(sID);
    }

    private void SetupLists() {
        m_vcHop = new HopAdditionAdapterViewControl(m_Controller);
        m_vcFermentable = new FermentableAdditionAdapterViewControl(m_Controller);
        m_vcYeasts = new YeastAdditionAdapterViewControl(m_Controller);

        m_HopAdapter = Tools.setupRecyclerView(lstHops, getApplicationContext(), R.layout.hop_list, R.layout.fragment_hop_dialog, false, m_vcHop, true);
        m_YeastAdapter = Tools.setupRecyclerView(lstYeasts, getApplicationContext(), R.layout.yeast_list, R.layout.fragment_yeast_dialog, false, m_vcYeasts, true);
        m_FermentableAdapter = Tools.setupRecyclerView(lstGrains, getApplicationContext(), R.layout.hop_list, R.layout.fragment_malt_dialog, false, m_vcFermentable, true);
        //TODO: Apparently I don't have adjuncts in the UI
        //m_AdjunctAdapter = Tools.setupRecyclerView(lstHops, getApplicationContext(), R.layout.hop_list, R.layout.fragment_hop_dialog, false, null, true);
    }

    private void FindUIElements() {
        lstGrains = findViewById(R.id.listGrains);
        lstHops = findViewById(R.id.listHops);
        lstYeasts = findViewById(R.id.listYeast);

        //stats card
        txtIBU = findViewById(R.id.txtIBUs);
        txtOG = findViewById(R.id.txtOG);
        txtFG = findViewById(R.id.txtFG);
        txtABV = findViewById(R.id.txtABV);
        txtSRM = findViewById(R.id.txtSRM);

        fabGrain = findViewById(R.id.fabGrain);
        fabGrain.setVisibility(View.INVISIBLE);
        fabHop = findViewById(R.id.fabHop);
        fabHop.setVisibility(View.INVISIBLE);
        fabYeast = findViewById(R.id.fabYeast);
        fabYeast.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save_recipe:
                //TODO: Do I need a save button anymore? Since the stats are calcualted on the server it probably needs to save automatically.
                /*if (CurrentRecipe != null) {
                    CurrentRecipe.ClearIngredients();
                    PopulateRecipeIngredients();
                    CurrentRecipe.Save();
                    return true;
                }*/
        }

        return true;
    }

    private <T> void PopulateAdditions(ArrayList<T> additions, NewAdapter adp) {
        adp.clear();

        for (int i = 0; i < additions.size(); i++) {
            adp.add((ListableObject) additions.get(i));
        }
    }

    private void SetButtonClickEvents() {
        //Main FAB
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AddToList(valGrains, "Test");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                if (!bShowExtraFab ) {
                    ShowExtraFAB();
                    fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                } else {
                    HideExtraFAB();
                    fab.setImageResource(android.R.drawable.ic_input_add);
                }
                bShowExtraFab = !bShowExtraFab;

            }
        });

        //Grain FAB
        FloatingActionButton fabGrain = findViewById(R.id.fabGrain);
        fabGrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_FermentableAdapter.showAddItemDialog(view.getContext(), null);
            }
        });

        //Hop FAB
        FloatingActionButton fabHop = findViewById(R.id.fabHop);
        fabHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_HopAdapter.showAddItemDialog(view.getContext(), null);
            }
        });

        //Yeast FAB
        FloatingActionButton fabYeast = findViewById(R.id.fabYeast);
        fabYeast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_YeastAdapter.showAddItemDialog(view.getContext(), null);
            }
        });
    }

    private void SetExtraFABHideEvents() {/*
        View screen = (View)findViewById(R.idString.coord);
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });

        View toolbar = (View)findViewById(R.idString.nestedscrollview);
        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });*/
/*
        lstGrains.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @CallSuper
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //super.onTouch(arg1);

                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });
*/

        /*
        lstHops.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });
        lstYeasts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //super.onTouch(arg0, arg1);
                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });*/
    }

    private void ShowExtraFAB() {
        int cx = fabGrain.getWidth() / 2;
        int cy = fabGrain.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(fabGrain, cx, cy, 0, finalRadius);
        fabGrain.setVisibility(View.VISIBLE);
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabHop, cx, cy, 0, finalRadius);
        fabHop.setVisibility(View.VISIBLE);
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabYeast, cx, cy, 0, finalRadius);
        fabYeast.setVisibility(View.VISIBLE);
        anim.start();

        //FloatingActionButton fab = (FloatingActionButton)findViewById(R.idString.fab);
        //fab.setImageIcon("@android:drawable/ic_input_add");

    }

    private void HideExtraFAB() {
        int cx = fabGrain.getWidth() / 2;
        int cy = fabGrain.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(fabGrain, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fabGrain.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabHop, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fabHop.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabYeast, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fabYeast.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    public void RefreshStats() {
        /*int dIBU = Calculations.CalculateIBU(m_HopAdditionAdapter, m_MaltAdapter);
        double dOG = Calculations.round(Calculations.CalculateOG(m_MaltAdapter), 5);
        double dFG = Calculations.round(Calculations.CalculateFG(m_MaltAdapter, m_YeastAdapter),5);
        double dABV = Calculations.round(Calculations.CalculateABV(m_MaltAdapter, m_YeastAdapter), 3);
        int dSRM = Calculations.CalculateSRM(m_MaltAdapter);

        txtIBU.setText("IBUs: " + dIBU);
        txtOG.setText("OG: " + dOG);
        txtFG.setText("FG: " + dFG);
        txtABV.setText("ABV: " + dABV + "%");
        txtSRM.setText("SRM: " + dSRM);

        View SRMCircle = findViewById(R.id.srmColor);
        if (CurrentRecipe != null) {
            CurrentRecipe.recipeStats.ibu = dIBU;
            CurrentRecipe.recipeStats.og = dOG;
            CurrentRecipe.recipeStats.fg = dFG;
            CurrentRecipe.recipeStats.abv = dABV;
            CurrentRecipe.recipeStats.srm = dSRM;

        }
        SRMCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Calculations.GetSRMColor(dSRM))));*/
    }

    @Override
    public void ShowToast(String sMessage) {
        Tools.ShowToast(getApplicationContext(), sMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void SetTitle(String sTitle) {
        toolbar.setTitle(sTitle);
    }

    @Override
    public void PopulateStats(RecipeStatistics recipeStats) {

    }

    @Override
    public void PopulateHops(ArrayList<HopAddition> hops) {
        PopulateAdditions(hops, m_HopAdapter);
    }

    @Override
    public void PopulateYeasts(ArrayList<Yeast> yeasts) {
        PopulateAdditions(yeasts, m_YeastAdapter);
    }

    @Override
    public void PopulateFermentables(ArrayList<FermentableAddition> fermentables) {
        PopulateAdditions(fermentables, m_FermentableAdapter);
    }

    @Override
    public void PopulateYeastDialog(ArrayList<Yeast> yeasts) {
        m_vcYeasts.PopulateList(yeasts);
    }

    @Override
    public void PopulateHopDialog(ArrayList<Hop> hops) {
        m_vcHop.PopulateList(hops);
    }

    @Override
    public void PopulateFermentableDialog(ArrayList<Fermentable> fermentables) {
        m_vcFermentable.PopulateList(fermentables);
    }

    @Override
    public void GetIngredients() {
        m_Controller.SetHops(m_HopAdapter.Dataset());
        m_Controller.SetFermentables(m_FermentableAdapter.Dataset());
        m_Controller.SetYeasts(m_YeastAdapter.Dataset());
    }
}
