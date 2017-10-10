package beer.unaccpetable.brewzilla.Screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import beer.unaccpetable.brewzilla.Adapters.HopAdditionAdapter;
import beer.unaccpetable.brewzilla.Adapters.FermentableAdditionAdapter;
import beer.unaccpetable.brewzilla.Adapters.YeastAdditionAdapter;
import beer.unaccpetable.brewzilla.Ingredients.FermentableAddition;
import beer.unaccpetable.brewzilla.Ingredients.HopAddition;
import beer.unaccpetable.brewzilla.Ingredients.Recipe;
import beer.unaccpetable.brewzilla.Ingredients.YeastAddition;
import beer.unaccpetable.brewzilla.Tools.Network;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.Calculations;
import beer.unaccpetable.brewzilla.Tools.Tools;

public class RecipeEditor extends AppCompatActivity {

    RecyclerView lstGrains, lstHops,lstYeasts;
    private RecyclerView.LayoutManager m_HopLayoutManager, m_YeastLayoutManager, m_MaltLayoutManager;
    private HopAdditionAdapter m_HopAdditionAdapter = new HopAdditionAdapter(R.layout.hop_list, R.layout.fragment_hop_dialog);
    private YeastAdditionAdapter m_YeastAdditionAdapter = new YeastAdditionAdapter(R.layout.yeast_list, R.layout.fragment_yeast_dialog);
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
        if (sID != null && sID.length() > 0) {
            //LoadRecipe(sID);
            LoadFullRecipe(sID);
//            return;
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
        CurrentRecipe.PopulateYeasts(m_YeastAdditionAdapter.Dataset());
    }

    private void LoadFullRecipe(String id) {
        /* Deployd bug: When there are more than 2 HopAdditions ( or Fermentables/Yeast ), it doesn't load in the Hop data after the second */
        /* So I'm changing it to load in everything separately */
        String sRecipeURL = Tools.RestAPIURL() + "/recipe?id=" + id; // + "&include=fullrecipe";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sRecipeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();

                final Gson gson = gsonBuilder.create();
                CurrentRecipe = gson.fromJson(response, Recipe.class);
                CurrentRecipe.Initiliaze();

                Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/hopaddition?recipeID=" + CurrentRecipe.id + "&include=hop", null,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                HopAddition[] hops = gson.fromJson(response, HopAddition[].class);

                                for (HopAddition h : hops) {
                                    m_HopAdditionAdapter.add(h);
                                    CurrentRecipe.hops.add(h);
                                }
                            }
                        }, null); //No error checking! Woo!

                Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/fermentableaddition?recipeID=" + CurrentRecipe.id + "&include=fermentables", null,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                FermentableAddition[] fermentables = gson.fromJson(response, FermentableAddition[].class);

                                for (FermentableAddition f : fermentables) {
                                    m_MaltAdapter.add(f);
                                    CurrentRecipe.fermentables.add(f);
                                }
                            }
                        }, null); //No error checking! Woo!

                Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/yeastaddition?recipeID=" + CurrentRecipe.id + "&include=yeast", null,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                YeastAddition[] yeasts = gson.fromJson(response, YeastAddition[].class);

                                for (YeastAddition y : yeasts) {
                                    m_YeastAdditionAdapter.add(y);
                                    CurrentRecipe.yeasts.add(y);
                                }
                            }
                        }, null); //No error checking! Woo!




                toolbar.setTitle(CurrentRecipe.name);
                RefreshStats();
                /*ArrayList<JSONObject> objs = Tools.GetJSONObjects(response);
                for(int i = 0; i < objs.size(); i++) {
                    JSONObject o = (JSONObject)objs.get(i);
                    String s = "Error";
                    String id = null;
                    try {
                        s = o.getString("name");
                        id = o.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    m_CurrentRecipe = new Recipe();
                    m_CurrentRecipe.Name = s;
                    m_CurrentRecipe.id = id;
                    toolbar.setTitle(m_CurrentRecipe.Name);
                }
                LoadHops(m_CurrentRecipe.id);*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        Network.getInstance(this).addToRequestQueue(stringRequest);
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
        lstYeasts.setAdapter(m_YeastAdditionAdapter);
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
                m_YeastAdditionAdapter.AddItem(RecipeEditor.this, null);
            }
        });
    }

    private void SetExtraFABHideEvents() {/*
        View screen = (View)findViewById(R.id.coord);
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

        View toolbar = (View)findViewById(R.id.nestedscrollview);
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

        //FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
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
        int dIBU = Calculations.CalculateIBU(m_HopAdditionAdapter, m_MaltAdapter);
        double dOG = Calculations.CalculateOG(m_MaltAdapter);
        double dFG = Calculations.CalculateFG(m_MaltAdapter, m_YeastAdditionAdapter);
        double dABV = Calculations.CalculateABV(m_MaltAdapter, m_YeastAdditionAdapter);
        int dSRM = Calculations.CalculateSRM(m_MaltAdapter);

        txtIBU.setText("IBUs: " + dIBU);
        txtOG.setText("OG: " + dOG);
        txtFG.setText("FG: " + dFG);
        txtABV.setText("ABV: " + dABV + "%");
        txtSRM.setText("SRM: " + dSRM);
    }

    /*private void LoadRecipe(String id) {
        String sRecipeURL = Tools.RestAPIURL() + "/recipe?id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sRecipeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                ArrayList<JSONObject> objs = Tools.GetJSONObjects(response);
                for(int i = 0; i < objs.size(); i++) {
                    JSONObject o = (JSONObject)objs.get(i);
                    String s = "Error";
                    String id = null;
                    try {
                        s = o.getString("name");
                        id = o.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    m_CurrentRecipe = new Recipe();
                    m_CurrentRecipe.name = s;
                    m_CurrentRecipe.id = id;
                    toolbar.setTitle(m_CurrentRecipe.name);
                }
                LoadHops(m_CurrentRecipe.id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        Network.getInstance(this).addToRequestQueue(stringRequest);
    }*/

    /*private void LoadHops(String RecipeID) {
        String sRecipeURL = Tools.RestAPIURL() + "/hopaddition?recipeID=" + RecipeID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sRecipeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                ArrayList<JSONObject> objs = Tools.GetJSONObjects(response);
                for(int i = 0; i < objs.size(); i++) {
                    JSONObject o = (JSONObject)objs.get(i);
                    String s = "no name..."; //object.getString("name");
                    double amt = 0;
                    String type = "";
                    int time = 0;
                    try {
                        amt = o.getDouble("amount");
                        type = o.getString("type");
                        time = o.getInt("time");
                    } catch (JSONException ex) {

                    }
                    double aau = 666; //TODO: This needs to come from the Hop table
                    Hop h = new Hop(s, amt, aau);
                    m_HopAdditionAdapter.add(h);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG);
            }
        });

        Network.getInstance(this).addToRequestQueue(stringRequest);
    }*/
}
