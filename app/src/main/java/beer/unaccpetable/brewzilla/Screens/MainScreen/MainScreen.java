package beer.unaccpetable.brewzilla.Screens.MainScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import beer.unaccpetable.brewzilla.Models.Recipe;

import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;
import beer.unaccpetable.brewzilla.Screens.BaseActivity;
import beer.unaccpetable.brewzilla.Screens.ImportBeerXML;
import beer.unaccpetable.brewzilla.Screens.IngredientManager;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditor;
import beer.unaccpetable.brewzilla.Screens.SettingsActivity;

public class MainScreen extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainScreenController.View {

    private RecyclerView lstRecipes;
    private NewAdapter m_Adapter;
    private MainScreenController m_Controller;

    TextView mTextView;

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
                CreateNewRecipe();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        m_Controller = new MainScreenController(new Repository());
        m_Controller.attachView(this);

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lstRecipes = findViewById(R.id.listRecipe);
        m_Adapter = Tools.setupRecyclerView(lstRecipes, getApplicationContext(), R.layout.one_line_list, 0, false, new RecipeListAdapterViewControl(this), true);

        mTextView = findViewById(R.id.mainscreen_text);

        m_Controller.LoadRecipes();
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_screen, menu);
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
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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


    private String CreateNewRecipe() {
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


        dialog.show();

        final Context c = this;

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText name = (EditText) dialog.findViewById(R.id.txtNewRecipeName);
                EditText style = (EditText) dialog.findViewById(R.id.txtNewRecipeStyle);
                String sName = name.getText().toString();
                String sStyle = style.getText().toString();

                dialog.dismiss();
                m_Controller.CreateNewRecipe(sName, sStyle);

            }
        });

        return "";
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
        startActivity(i);
    }
}
