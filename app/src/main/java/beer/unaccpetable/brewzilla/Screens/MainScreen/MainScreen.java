package beer.unaccpetable.brewzilla.Screens.MainScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import beer.unaccpetable.brewzilla.Models.Recipe;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;
import beer.unaccpetable.brewzilla.Screens.BaseActivity;
import beer.unaccpetable.brewzilla.Screens.IngredientManager.IngredientManager;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditor;
import beer.unaccpetable.brewzilla.Screens.SettingsActivity;

public class MainScreen extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainScreenController.View {

    private RecyclerView lstRecipes;
    private NewAdapter m_Adapter;
    private MainScreenController m_Controller;

    private SwipeRefreshLayout m_SwipeRefresh;

    //new intent request codes
    public static final int EDIT_RECIPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                m_Controller.ShowNewRecipeDialog();
                //CreateNewRecipe();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FindUIElements();

        m_Controller = new MainScreenController(new Repository());
        m_Controller.attachView(this);

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lstRecipes = findViewById(R.id.listRecipe);
        m_Adapter = Tools.setupRecyclerView(lstRecipes, getApplicationContext(), R.layout.list_recipe, 0, false, new RecipeListAdapterViewControl(this), true);

        m_Controller.LoadRecipes(false);

        m_SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_Controller.LoadRecipes(true);
            }
        });
    }

    private void FindUIElements() {
        m_SwipeRefresh = findViewById(R.id.content_main_screen);
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
        if (id == R.id.refresh) {
            m_SwipeRefresh.setRefreshing(true);
            m_Controller.LoadRecipes(true);
            /*Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);*/
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

        /*if (id == R.id.nav_create_recipe) {
            CreateNewRecipe();
        } else if (id == R.id.nav_brew_beer) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {


        } else */
        if (id == R.id.nav_ingredient_manager) {
            intNextScreen = new Intent(this, IngredientManager.class);
        } else if (id == R.id.nav_signout) {
            SharedPreferences sharedPreferences = getSharedPreferences("Prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("APIToken");
            editor.commit();
            Tools.LaunchSignInScreen(this, MainScreen.class);
        } else if (id == R.id.nav_settings) {
            intNextScreen = new Intent(this, SettingsActivity.class);
        }

        /*else if (id == R.id.nav_import_beerxml) {
            intNextScreen = new Intent(this, ImportBeerXML.class);

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (intNextScreen != null)
            startActivity(intNextScreen);

        return true;
    }

    @Override
    public void ShowNewRecipeDialog(Style[] styles) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setView(m_iDialogLayout);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.fragment_newrecipe_dialog, null);
        builder.setView(root);
        final AlertDialog dialog = builder.create();

        Spinner spStyle = root.findViewById(R.id.spinStyle);
        Tools.PopulateDropDown(spStyle, getApplicationContext(), styles);

        dialog.show();

        final Context c = this;

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText name = (EditText) dialog.findViewById(R.id.txtNewRecipeName);
                Spinner style = dialog.findViewById(R.id.spinStyle);
                String sName = name.getText().toString();
                //String sStyle = style.getText().toString();
                Style s = (Style)style.getSelectedItem();

                dialog.dismiss();
                m_Controller.CreateNewRecipe(sName, s);

            }
        });


    }

    @Override
    public void PopulateRecipeList(Recipe[] r) {
        m_Adapter.clear();

        for (Recipe recipe : r) {
            m_Adapter.add(recipe);
        }
    }

    @Override
    public void ShowToast(String sMessage) {
        Tools.ShowToast(getApplicationContext(), sMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void OpenRecipe(String sIDString) {
        Intent i = new Intent(getApplicationContext(), RecipeEditor.class);
        i.putExtra("RecipeID", sIDString);
        //startActivity(i);
        startActivityForResult(i, EDIT_RECIPE);
    }

    @Override
    public void StopRefresh() {
        m_SwipeRefresh.setRefreshing(false);
    }

    @Override
    public void AddRecipe(Recipe r){
        m_Adapter.add(r);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EDIT_RECIPE:
                    String sIDString = data.getStringExtra("idString");
                    double dAbv = data.getDoubleExtra("abv", 0);
                    boolean bDeleted = data.getBooleanExtra("deleted", false);
                    String sStyleName = data.getStringExtra("styleName");

                    for (int i = 0; i < m_Adapter.size(); i++) {
                        if (m_Adapter.get(i).idString.equals(sIDString)) {
                            if (bDeleted) {
                                m_Adapter.remove(i);
                            } else {
                                Recipe r = (Recipe) m_Adapter.get(i);
                                r.recipeStats.abv = dAbv;
                                if (r.style == null)
                                    r.style = new Style(); //for old bad data
                                r.style.name = sStyleName; //TODO: Should this pass back the whole style, not just the name?
                                //m_Adapter.notifyDataSetChanged();
                                m_Adapter.notifyItemChanged(i);
                            }
                        }
                    }
            }
        }
    }
}
