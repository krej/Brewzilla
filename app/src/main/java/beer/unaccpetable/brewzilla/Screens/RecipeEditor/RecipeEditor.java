package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import beer.unaccpetable.brewzilla.Tools.Calculations;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

public class RecipeEditor extends BaseActivity implements RecipeEditorController.View {

    private RecipeEditorController m_Controller;

    RecyclerView lstGrains, lstHops,lstYeasts;
    NewAdapter m_HopAdapter, m_YeastAdapter, m_FermentableAdapter, m_AdjunctAdapter;
    HopAdditionAdapterViewControl m_vcHop;
    FermentableAdditionAdapterViewControl m_vcFermentable;
    YeastAdditionAdapterViewControl m_vcYeasts;

    private Boolean bShowExtraFab = false;
    FloatingActionButton fabGrain,fabHop, fabYeast, fabSRM;

    private TextView txtIBU, txtOG, txtFG, txtABV, txtSRM;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FindUIElements();

        fabHop.setImageResource(R.drawable.ic_hop);
        fabGrain.setImageResource(R.drawable.ic_grain);
        fabYeast.setImageResource(R.drawable.ic_test_tube);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SetButtonClickEvents();

        m_Controller = new RecipeEditorController(new Repository());
        m_Controller.attachView(this);

        SetupLists();

        String sID = getIntent().getStringExtra("RecipeID");

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
        fabSRM = findViewById(R.id.srmColor);

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
        //inflater.inflate(R.menu.menu_create_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save_recipe:
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

    @Override
    public void ShowToast(String sMessage) {
        Tools.ShowToast(getApplicationContext(), sMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void SetTitle(String sTitle) {
        toolbar.setTitle(sTitle);
    }

    @Override
    public void PopulateStats(RecipeStatistics stats) {
        txtIBU.setText("IBUs: " + stats.getFormattedIBU());
        txtOG.setText("OG: " + stats.getFormattedOG());
        txtFG.setText("FG: " + stats.getFormattedFG());
        txtABV.setText("ABV: " + stats.getFormatredAbv() + "%");
        txtSRM.setText("SRM: " + stats.getFormattedSRM());
        fabSRM.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Calculations.GetSRMColor((int)stats.srm))));
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
