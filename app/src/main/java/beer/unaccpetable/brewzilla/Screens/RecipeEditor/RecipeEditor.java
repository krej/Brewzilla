package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.gesture.Gesture;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import beer.unaccpetable.brewzilla.Models.RecipeParameters;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;
import beer.unaccpetable.brewzilla.Screens.BaseActivity;
import beer.unaccpetable.brewzilla.Tools.Calculations;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

public class RecipeEditor extends BaseActivity implements RecipeEditorController.View {

    private RecipeEditorController m_Controller;


    /********** Recipe Editor tab *************/
    RecyclerView lstGrains, lstHops,lstYeasts;
    NewAdapter m_HopAdapter, m_YeastAdapter, m_FermentableAdapter, m_AdjunctAdapter;
    HopAdditionAdapterViewControl m_vcHop;
    FermentableAdditionAdapterViewControl m_vcFermentable;
    YeastAdditionAdapterViewControl m_vcYeasts;

    private Boolean bShowExtraFab = false;
    FloatingActionButton fabGrain,fabHop, fabYeast, fabSRM;
    FloatingActionButton fabMain;

    private TextView txtIBU, txtOG, txtFG, txtABV, txtSRM;
    Spinner m_spStyle;
    Toolbar toolbar;

    private ViewFlipper m_ViewFlipper;
    TabLayout m_TabLayout;
    TabItem m_tabRecipe, m_tabMash;

    /******** Mash tab *************/
    //initial mash
    Spinner m_spGristRatio;
    EditText m_txtInitialMashTemp;
    TextView m_lblInitialStrikeTemp;
    TextView m_lblInitialStrikeVolume;
    TextView m_txtTargetMashTemp;
    //mash infusion
    EditText m_txtCurrentTempOfMash, m_txtTargetMashTempInfusion, m_txtTotalWaterInMash, m_txtHLTTemp;
    TextView m_lblWaterToAdd;
    LinearLayout m_llMashInfusionWaterToAdd;

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
        SetupRecipeParamaterListeners();
        SetupStyleListener();

        SetupTabChangeListener();

        m_Controller = new RecipeEditorController(new Repository());
        m_Controller.attachView(this);

        SetupLists();

        String sID = getIntent().getStringExtra("RecipeID");

        m_Controller.LoadRecipe(sID);
    }

    private void SetupStyleListener() {
        m_spStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Style style = (Style)parent.getSelectedItem();
                m_Controller.SetStyle(style);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SetupTabChangeListener() {
        final Activity a = this;

        m_TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideKeyboard(a);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void hideKeyboard(Activity a) {
        InputMethodManager imm = (InputMethodManager) a.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = a.getCurrentFocus();
        if (v == null)
            v = new View(a);

        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void SetupRecipeParamaterListeners() {
        m_spGristRatio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] gristValues = getResources().getStringArray(R.array.gristRatioValues);
                double dValue = Tools.ParseDouble(gristValues[position]);

                m_Controller.setGristRatio(dValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        m_txtInitialMashTemp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double dTemp = Tools.ParseDouble(s.toString());
                m_Controller.SetInitialMashTemp(dTemp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        m_txtTargetMashTemp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double dTemp = Tools.ParseDouble(s.toString());
                m_Controller.SetTargetMashTemp(dTemp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextWatcher twMashInfusion = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sCurrentTemp = m_txtCurrentTempOfMash.getText().toString();
                String sTargetMashTemp = m_txtTargetMashTempInfusion.getText().toString();
                String sTotalWaterInMash = m_txtTotalWaterInMash.getText().toString();
                String sHLTTemp = m_txtHLTTemp.getText().toString();

                m_Controller.CalcMashInfusion(sCurrentTemp, sTargetMashTemp, sTotalWaterInMash, sHLTTemp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        m_txtCurrentTempOfMash.addTextChangedListener(twMashInfusion);
        m_txtTargetMashTempInfusion.addTextChangedListener(twMashInfusion);
        m_txtTotalWaterInMash.addTextChangedListener(twMashInfusion);
        m_txtHLTTemp.addTextChangedListener(twMashInfusion);
    }

    public void SwitchToMashView() {
        m_ViewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_left);
        m_ViewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_left);
        m_ViewFlipper.setDisplayedChild(m_ViewFlipper.indexOfChild(findViewById(R.id.mashView)));
        /*fabYeast.setVisibility(View.GONE);
        fabHop.setVisibility(View.GONE);
        fabGrain.setVisibility(View.GONE);*/
        HideExtraFAB();
        fabMain.setVisibility(View.GONE);
        bShowExtraFab = false;
    }

    public void SwitchToRecipeView() {

        m_ViewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_right);
        m_ViewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_right);
        m_ViewFlipper.setDisplayedChild(m_ViewFlipper.indexOfChild(findViewById(R.id.recipeView)));
        fabMain.setVisibility(View.VISIBLE);
        fabMain.setImageResource(android.R.drawable.ic_input_add);
    }

    @Override
    public void PopulateParameters(RecipeParameters recipeParameters) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gristRatioValues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //m_spGristRatio.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(String.valueOf(recipeParameters.gristRatio));
        m_spGristRatio.setSelection(spinnerPosition);
        m_txtInitialMashTemp.setText(String.valueOf(recipeParameters.initialMashTemp));
        m_txtTargetMashTemp.setText(String.valueOf(recipeParameters.targetMashTemp));

        //mash infusion
        m_txtCurrentTempOfMash.setText(String.valueOf(recipeParameters.targetMashTemp));
        m_txtTargetMashTempInfusion.setText(String.valueOf(recipeParameters.targetMashTemp));
        //m_txtTotalWaterInMash.setText
        //m_txtHLTTemp.setText();
    }

    private void SetupLists() {
        m_vcHop = new HopAdditionAdapterViewControl(m_Controller);
        m_vcFermentable = new FermentableAdditionAdapterViewControl(m_Controller);
        m_vcYeasts = new YeastAdditionAdapterViewControl(m_Controller);

        m_HopAdapter = Tools.setupRecyclerView(lstHops, getApplicationContext(), R.layout.hop_list, R.layout.fragment_hop_dialog, false, m_vcHop, true, false, true);
        m_YeastAdapter = Tools.setupRecyclerView(lstYeasts, getApplicationContext(), R.layout.yeast_list, R.layout.fragment_yeast_dialog, false, m_vcYeasts, true, false, true);
        m_FermentableAdapter = Tools.setupRecyclerView(lstGrains, getApplicationContext(), R.layout.hop_list, R.layout.fragment_malt_dialog, false, m_vcFermentable, true, false, true);

        m_HopAdapter.setNotifySwipeDelete(createNotifySwipeDeleteAdapter(m_HopAdapter));
        m_FermentableAdapter.setNotifySwipeDelete(createNotifySwipeDeleteAdapter(m_FermentableAdapter));
        m_YeastAdapter.setNotifySwipeDelete(createNotifySwipeDeleteAdapter(m_YeastAdapter));

        //TODO: Apparently I don't have adjuncts in the UI
        //m_AdjunctAdapter = Tools.setupRecyclerView(lstHops, getApplicationContext(), R.layout.hop_list, R.layout.fragment_hop_dialog, false, null, true);
    }

    private NewAdapter.INotifySwipeDelete createNotifySwipeDeleteAdapter(NewAdapter adapter) {
        NewAdapter.INotifySwipeDelete nsd = new NewAdapter.INotifySwipeDelete() {

            @Override
            public void notifyDelete(int position, ListableObject i) {
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.coord), R.string.item_deleted, Snackbar.LENGTH_SHORT);

                mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.add(position, i);
                        m_Controller.RecipeUpdated();
                    }
                });
                mySnackbar.show();

                m_Controller.RecipeUpdated();
            }
        };

        return nsd;
    }

    private void FindUIElements() {
        //Recipe Tab
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
        m_spStyle = findViewById(R.id.spinStyle);

        fabGrain = findViewById(R.id.fabGrain);
        fabGrain.setVisibility(View.INVISIBLE);
        fabHop = findViewById(R.id.fabHop);
        fabHop.setVisibility(View.INVISIBLE);
        fabYeast = findViewById(R.id.fabYeast);
        fabYeast.setVisibility(View.INVISIBLE);

        m_ViewFlipper = findViewById(R.id.recipeEditorViewFlipper);

        m_TabLayout = findViewById(R.id.tabRecipeEditor);
        m_tabMash = findViewById(R.id.tabMash);
        m_tabRecipe = findViewById(R.id.tabRecipe);
        fabMain = findViewById(R.id.fab);

        //Mash Tab
        m_spGristRatio = findViewById(R.id.spinGristRatio);
        m_txtInitialMashTemp = findViewById(R.id.txtInitialMashTemp);
        m_lblInitialStrikeTemp = findViewById(R.id.mashStrikeTemp);
        m_lblInitialStrikeVolume = findViewById(R.id.mashStrikeVolume);
        m_txtTargetMashTemp = findViewById(R.id.txtTargetMashTemp);

        m_txtCurrentTempOfMash = findViewById(R.id.txtCurrentTempOfMash);
        m_txtTargetMashTempInfusion = findViewById(R.id.txtMashTargetTempInfusion);
        m_txtTotalWaterInMash = findViewById(R.id.txtTotalWaterInMash);
        m_txtHLTTemp = findViewById(R.id.txtHltTemp);
        m_lblWaterToAdd = findViewById(R.id.waterToAdd);
        m_llMashInfusionWaterToAdd = findViewById(R.id.llMashInfusionWaterToAdd);
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

        return super.onOptionsItemSelected(item);
    }

    private <T> void PopulateAdditions(ArrayList<T> additions, NewAdapter adp) {
        adp.clear();

        for (int i = 0; i < additions.size(); i++) {
            adp.add((ListableObject) additions.get(i));
        }
    }

    private void SetButtonClickEvents() {
        //Main FAB
        //final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!bShowExtraFab ) {
                    ShowExtraFAB();
                    fabMain.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                } else {
                    HideExtraFAB();
                    fabMain.setImageResource(android.R.drawable.ic_input_add);
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

        m_TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                m_Controller.TabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
        //recipe tab
        txtIBU.setText("IBUs: " + stats.getFormattedIBU());
        txtOG.setText("OG: " + stats.getFormattedOG());
        txtFG.setText("FG: " + stats.getFormattedFG());
        txtABV.setText("ABV: " + stats.getFormatredAbv() + "%");
        txtSRM.setText("SRM: " + stats.getFormattedSRM());
        fabSRM.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Calculations.GetSRMColor((int)stats.srm))));

        //mash tab
        m_lblInitialStrikeTemp.setText(stats.getFormattedStrikeWaterTemp());
        m_lblInitialStrikeVolume.setText(stats.getFormattedStrikeWaterVolume() + " quarts");

        m_txtTotalWaterInMash.setText(stats.getFormattedStrikeWaterVolume());
        m_txtHLTTemp.setText(stats.getFormattedStrikeWaterTemp());
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

    @Override
    public void MashInfusionShowWaterToAdd(String sVolume) {
        m_llMashInfusionWaterToAdd.setVisibility(View.VISIBLE);
        m_lblWaterToAdd.setText(sVolume);
    }

    @Override
    public void PopulateStyleDropDown(Style[] styles) {
        Tools.PopulateDropDown(m_spStyle, getApplicationContext(), styles);
    }

    @Override
    public void SetStyle(Style[] styles, Style beerStyle) {
        Tools.SetDropDownSelection(m_spStyle, styles, beerStyle);
    }
}
