package beer.unaccpetable.brewzilla;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Adapters.HopAdapter;
import beer.unaccpetable.brewzilla.Adapters.MaltAdapter;
import beer.unaccpetable.brewzilla.Adapters.YeastAdapter;
import beer.unaccpetable.brewzilla.Ingredients.FermentableAddition;
import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.HopAddition;
import beer.unaccpetable.brewzilla.Ingredients.Recipe;
import beer.unaccpetable.brewzilla.Ingredients.YeastAddition;
import beer.unaccpetable.brewzilla.Tools.Calculations;
import beer.unaccpetable.brewzilla.Tools.Tools;

public class NewRecipe extends AppCompatActivity {

    RecyclerView lstGrains, lstHops,lstYeasts;
    private RecyclerView.LayoutManager m_HopLayoutManager, m_YeastLayoutManager, m_MaltLayoutManager;
    private HopAdapter m_HopAdapter = new HopAdapter(R.layout.hop_list, R.layout.fragment_hop_dialog);
    private YeastAdapter m_YeastAdapter = new YeastAdapter(R.layout.yeast_list, R.layout.fragment_yeast_dialog);
    private MaltAdapter m_MaltAdapter = new MaltAdapter(R.layout.hop_list, R.layout.fragment_malt_dialog);

    private Boolean bShowExtraFab = false;
    View fabGrain,fabHop, fabYeast;

    private TextView txtIBU, txtOG, txtFG, txtABV, txtSRM;
    Toolbar toolbar;

    private Recipe r;

    Recipe m_CurrentRecipe;
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
        SetUpMaltList();

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

    private void LoadFullRecipe(String id) {
        String sRecipeURL = Tools.RestAPIURL() + "/recipe?id=" + id + "&include=fullrecipe";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sRecipeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();

                Gson gson = gsonBuilder.create();
                r = gson.fromJson(response, Recipe.class);

                for (HopAddition h : r.hops) {
                    m_HopAdapter.add(h);
                }

                for (YeastAddition y :r.yeasts) {
                    m_YeastAdapter.add(y);
                }

                for (FermentableAddition f : r.fermentables) {
                    m_MaltAdapter.add(f);
                }

                toolbar.setTitle(r.name);
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
                Toast.makeText(view.getContext(), "Test" + m_HopAdapter.clickedPosition(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetUpHopList() {
        lstHops.setHasFixedSize(false);
        m_HopLayoutManager = new LinearLayoutManager(this);
        lstHops.setLayoutManager(m_HopLayoutManager);
        lstHops.setAdapter(m_HopAdapter);
        //m_HopAdapter.add(new Hop("Citra", 1.5, 13.65, 20));
    }
    private void SetUpYeastList() {
        lstYeasts.setHasFixedSize(false);
        m_YeastLayoutManager = new LinearLayoutManager(this);
        lstYeasts.setLayoutManager(m_YeastLayoutManager);
        lstYeasts.setAdapter(m_YeastAdapter);
        //m_YeastAdapter.add(new Yeast("1056", "Wyeast", 75));
    }
    private void SetUpMaltList() {
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
                m_MaltAdapter.AddItem(NewRecipe.this, null);
            }
        });

        //Hop FAB
        FloatingActionButton fabHop = (FloatingActionButton) findViewById(R.id.fabHop);
        fabHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_HopAdapter.AddItem(NewRecipe.this, null);
            }
        });

        //Yeast FAB
        FloatingActionButton fabYeast = (FloatingActionButton) findViewById(R.id.fabYeast);
        fabYeast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_YeastAdapter.AddItem(NewRecipe.this, null);
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
        int dIBU = Calculations.CalculateIBU(m_HopAdapter, m_MaltAdapter);
        double dOG = Calculations.CalculateOG(m_MaltAdapter);
        double dFG = Calculations.CalculateFG(m_MaltAdapter, m_YeastAdapter);
        double dABV = Calculations.CalculateABV(m_MaltAdapter, m_YeastAdapter);
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
                    m_HopAdapter.add(h);
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
