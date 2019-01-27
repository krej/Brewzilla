package beer.unaccpetable.brewzilla.Screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Adapters.Adapter;
import beer.unaccpetable.brewzilla.Adapters.HopAdditionAdapter;
import beer.unaccpetable.brewzilla.Adapters.FermentableAdditionAdapter;
import beer.unaccpetable.brewzilla.Adapters.YeastAdapter;
import beer.unaccpetable.brewzilla.Adapters.YeastAdditionAdapter;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.YeastAddition;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Network;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.Calculations;
import beer.unaccpetable.brewzilla.Tools.Tools;

public class RecipeEditor extends AppCompatActivity {

    RecyclerView lstGrains, lstHops,lstYeasts;
    private RecyclerView.LayoutManager m_HopLayoutManager, m_YeastLayoutManager, m_MaltLayoutManager;
    private HopAdditionAdapter m_HopAdditionAdapter = new HopAdditionAdapter(R.layout.hop_list, R.layout.fragment_hop_dialog);
    //private YeastAdditionAdapter m_YeastAdditionAdapter = new YeastAdditionAdapter(R.layout.yeast_list, R.layout.fragment_yeast_dialog);
    private YeastAdapter m_YeastAdapter = new YeastAdapter(R.layout.yeast_list, R.layout.fragment_yeast_dialog);
    private FermentableAdditionAdapter m_MaltAdapter = new FermentableAdditionAdapter(R.layout.hop_list, R.layout.fragment_malt_dialog);

    private Boolean bShowExtraFab = false;
    View fabGrain,fabHop, fabYeast;

    private TextView txtIBU, txtOG, txtFG, txtABV, txtSRM;
    Toolbar toolbar;

    public Recipe CurrentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        lstGrains = (RecyclerView) findViewById(R.id.listGrains);
        lstHops = (RecyclerView)findViewById(R.id.listHops);
        lstYeasts = (RecyclerView)findViewById(R.id.listYeast);

        //stats card
        txtIBU = (TextView)findViewById(R.id.txtIBUs);
        txtOG = (TextView)findViewById(R.id.txtOG);
        txtFG = (TextView)findViewById(R.id.txtFG);
        txtABV = (TextView)findViewById(R.id.txtABV);
        txtSRM = (TextView)findViewById(R.id.txtSRM);

        fabGrain = findViewById(R.id.fabGrain);
        fabGrain.setVisibility(View.INVISIBLE);
        fabHop = findViewById(R.id.fabHop);
        fabHop.setVisibility(View.INVISIBLE);
        fabYeast = findViewById(R.id.fabYeast);
        fabYeast.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SetExtraFABHideEvents();

        SetButtonClickEvents();
        SetRecyclerViewClickEvents();

        SetUpHopList();
        SetUpYeastList();
        SetUpFermentableList();

        String sID = getIntent().getStringExtra("RecipeID");
        //Recipe r = (Recipe) getIntent().getSerializableExtra("Recipe"); //For some reason this comes in as name = "Empty". i dont know why its not getting it correctly
        String sRecipeName = getIntent().getStringExtra("name");
        String sRecipeStyle = getIntent().getStringExtra("style"); // not sure where to use this yet...
        if (sID != null && sID.length() > 0) {
            //LoadRecipe(sID);
            LoadFullRecipe(sID);
//            return;
        } else if (sRecipeName != null && sRecipeName.length() > 0) {
            toolbar.setTitle(sRecipeName);
        } else {
            toolbar.setTitle("Create Recipe");
        }

        RefreshStats();


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
                if (CurrentRecipe != null) {
                    CurrentRecipe.ClearIngredients();
                    PopulateRecipeIngredients();
                    CurrentRecipe.Save();
                    return true;
                }
        }

        return true;
    }

    private void PopulateRecipeIngredients() {
        CurrentRecipe.PopulateHops(m_HopAdditionAdapter.Dataset());
        CurrentRecipe.PopulateFermentables(m_MaltAdapter.Dataset());
        CurrentRecipe.PopulateYeasts(m_YeastAdapter.Dataset());
    }

    private void LoadFullRecipe(String id) {
        /* Deployd bug: When there are more than 2 HopAdditions ( or Fermentable/Yeast ), it doesn't load in the Hop data after the second */
        /* So I'm changing it to load in everything separately */
        String sRecipeURL = Tools.RestAPIURL() + "/recipe/" + id; // + "&include=fullrecipe";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sRecipeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();

                final Gson gson = gsonBuilder.create();
                CurrentRecipe = gson.fromJson(response, Recipe.class);
                CurrentRecipe.Initiliaze(); //TODO: Is this needed?

                PopulateAdditions(CurrentRecipe.hops, m_HopAdditionAdapter);
                PopulateAdditions(CurrentRecipe.fermentables, m_MaltAdapter);
                PopulateAdditions(CurrentRecipe.yeasts, m_YeastAdapter);
                //PopulateAdditions(CurrentRecipe.adjuncts, m_AdjunctAdapter);
/*
                Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/hopaddition?recipeID=" + CurrentRecipe.idString + "&include=hop", null,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                HopAddition[] hops = gson.fromJson(response, HopAddition[].class);

                                for (HopAddition h : hops) {
                                    m_HopAdditionAdapter.add(h);
                                    CurrentRecipe.hops.add(h);
                                }

                                RefreshStats();
                            }
                        }, null); //No error checking! Woo!

                Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/fermentableaddition?recipeID=" + CurrentRecipe.idString + "&include=fermentable", null,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                FermentableAddition[] fermentables = gson.fromJson(response, FermentableAddition[].class);

                                for (FermentableAddition f : fermentables) {
                                    m_MaltAdapter.add(f);
                                    CurrentRecipe.fermentables.add(f);
                                }

                                RefreshStats();
                            }
                        }, null); //No error checking! Woo!

                Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/yeastaddition?recipeID=" + CurrentRecipe.idString + "&include=yeast", null,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                YeastAddition[] yeasts = gson.fromJson(response, YeastAddition[].class);

                                for (YeastAddition y : yeasts) {
                                    m_YeastAdditionAdapter.add(y);
                                    CurrentRecipe.yeasts.add(y);
                                }

                                RefreshStats();
                            }
                        }, null); //No error checking! Woo!
*/



                toolbar.setTitle(CurrentRecipe.name);
                //RefreshStats();
                /*ArrayList<JSONObject> objs = Tools.GetJSONObjects(response);
                for(int i = 0; i < objs.size(); i++) {
                    JSONObject o = (JSONObject)objs.get(i);
                    String s = "Error";
                    String idString = null;
                    try {
                        s = o.getString("name");
                        idString = o.getString("idString");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    m_CurrentRecipe = new Recipe();
                    m_CurrentRecipe.Name = s;
                    m_CurrentRecipe.idString = idString;
                    toolbar.setTitle(m_CurrentRecipe.Name);
                }
                LoadHops(m_CurrentRecipe.idString);*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        Network.getInstance(this).addToRequestQueue(stringRequest);
    }

    private <T> void PopulateAdditions(ArrayList<T> additions, Adapter adp) {
        for (int i = 0; i < additions.size(); i++) {
            adp.add((ListableObject) additions.get(i));
        }
    }

    private void SetRecyclerViewClickEvents() {
        lstHops.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Test" + m_HopAdditionAdapter.clickedPosition(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetUpHopList() {
        lstHops.setHasFixedSize(false);
        m_HopLayoutManager = new LinearLayoutManager(this);
        lstHops.setLayoutManager(m_HopLayoutManager);
        lstHops.setAdapter(m_HopAdditionAdapter);
        //m_HopAdditionAdapter.add(new Hop("Citra", 1.5, 13.65, 20));
    }
    private void SetUpYeastList() {
        lstYeasts.setHasFixedSize(false);
        m_YeastLayoutManager = new LinearLayoutManager(this);
        lstYeasts.setLayoutManager(m_YeastLayoutManager);
        lstYeasts.setAdapter(m_YeastAdapter);
        //m_YeastAdditionAdapter.add(new Yeast("1056", "Wyeast", 75));
    }
    private void SetUpFermentableList() {
        lstGrains.setHasFixedSize(false);
        m_MaltLayoutManager = new LinearLayoutManager(this);
        lstGrains.setLayoutManager(m_MaltLayoutManager);
        lstGrains.setAdapter(m_MaltAdapter);
        //m_MaltAdapter.add(new Malt("2 Row", 10, 37, 1));
    }

    private void SetButtonClickEvents() {
        //Main FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AddToList(valGrains, "Test");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                if (!bShowExtraFab ) {
                    ShowExtraFAB();
                } else {
                    HideExtraFAB();
                }
                bShowExtraFab = !bShowExtraFab;

            }
        });

        //Grain FAB
        FloatingActionButton fabGrain = (FloatingActionButton) findViewById(R.id.fabGrain);
        fabGrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_MaltAdapter.AddItem(RecipeEditor.this, null);
            }
        });

        //Hop FAB
        FloatingActionButton fabHop = (FloatingActionButton) findViewById(R.id.fabHop);
        fabHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_HopAdditionAdapter.AddItem(RecipeEditor.this, null);
            }
        });

        //Yeast FAB
        FloatingActionButton fabYeast = (FloatingActionButton) findViewById(R.id.fabYeast);
        fabYeast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_YeastAdapter.AddItem(RecipeEditor.this, null);
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
}
