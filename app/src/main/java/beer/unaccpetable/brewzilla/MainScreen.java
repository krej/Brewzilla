package beer.unaccpetable.brewzilla;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import beer.unaccpetable.brewzilla.Adapters.Adapter;
import beer.unaccpetable.brewzilla.Adapters.HopAdapter;
import beer.unaccpetable.brewzilla.Adapters.RecipeAdapter;
import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Recipe;
import beer.unaccpetable.brewzilla.Tools.Tools;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView.LayoutManager m_RecipeLayoutManager;
    private RecyclerView lstRecipes;
    private RecipeAdapter m_RecipeAdapter = new RecipeAdapter(R.layout.recipe_list, 0);
    private String m_sRestAPIURL = Tools.RestAPIURL();

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lstRecipes = (RecyclerView) findViewById(R.id.listRecipe);
        SetUpHopList();

        mTextView = (TextView) findViewById(R.id.mainscreen_text);

        GetRecipes();
    }

    private void GetRecipes() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String sRecipeURL = m_sRestAPIURL + "/recipe";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sRecipeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //mTextView.setText("Respone is :" + response);// + response.substring(0, 500));
                SetRecipeList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work " + error.getMessage());
            }
        });

        Network.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void SetRecipeList(String json) {
        //GsonBuilder gsonBuilder = new GsonBuilder();

        //Gson gson = gsonBuilder.create();
        //Recipe r = gson.fromJson(json, Recipe.class);
        json = json.replace("[", "[\n");
        json = json.replace("]", "]\n");
        json = json.replace("},", "}\n");
        //m_RecipeAdapter.clear();
        try (StringReader sr = new StringReader(json); BufferedReader in = new BufferedReader(sr)) {
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    if (line.equals("[") || line.equals("]")) continue;
                    JSONObject object = new JSONObject(line);
                    String s = object.getString("name");
                    String id = object.getString("id");
                    Recipe r = new Recipe();
                    r.name = s;
                    r.id = id;
                    m_RecipeAdapter.add(r);
                }
            } catch (JSONException ex) {

            }
        } catch(IOException e) {

        }
    }

    private void SetUpHopList() {
        lstRecipes.setHasFixedSize(false);
        m_RecipeLayoutManager = new LinearLayoutManager(this);
        lstRecipes.setLayoutManager(m_RecipeLayoutManager);
        lstRecipes.setAdapter(m_RecipeAdapter);
        Recipe r = new Recipe();
        r.name = "test";
        //m_RecipeAdapter.add(r);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intNextScreen = null;

        if (id == R.id.nav_create_recipe) {
            intNextScreen = new Intent(this, NewRecipe.class);
        } else if (id == R.id.nav_brew_beer) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (intNextScreen != null)
            startActivity(intNextScreen);

        return true;
    }

    public void AddRecipe(View v) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String sRecipeURL = m_sRestAPIURL + "/recipe";
        JSONObject o = new JSONObject();
        StringRequest r = null;
        try {
            o.put("name", "hello!!!");
            final String s = o.toString();

            r = new StringRequest(Request.Method.POST, sRecipeURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    GetRecipes();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String s = error.getMessage();
mTextView.setText("Error!" + error.getMessage());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return s == null ? null : s.getBytes("utf-8");
                    } catch ( UnsupportedEncodingException ex) {
                        VolleyLog.wtf("Unsupportshit");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

        } catch (JSONException e) {

        }

        queue.add(r);
    }
}
