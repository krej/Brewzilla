package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;
import beer.unaccpetable.brewzilla.Screens.BaseActivity;
import beer.unaccpetable.brewzilla.Screens.ViewBrewLog.ViewBrewLog;

import com.unacceptable.unacceptablelibrary.Repositories.TimeSource;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

public class RecipeEditor extends BaseActivity implements RecipeEditorController.View {

    private RecipeEditorController m_Controller;

    Toolbar toolbar;
    AppBarLayout appBarLayout;

    TabLayout m_TabLayout;
    ViewPager m_ViewPager;

    RecipeEditorPagerAdapter recipeEditorPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FindUIElements();

        m_Controller = new RecipeEditorController(new Repository(), new TimeSource());
        m_Controller.attachView(this);

        recipeEditorPagerAdapter = new RecipeEditorPagerAdapter(getSupportFragmentManager(), m_Controller.getRecipeViewController());
        m_ViewPager.setAdapter(recipeEditorPagerAdapter);
        m_TabLayout.setupWithViewPager(m_ViewPager);

        SetupTabChangeListener();

        String sID = getIntent().getStringExtra("RecipeID");
        m_Controller.LoadRecipe(sID);
        m_Controller.LoadBrewLogs(sID);
        //m_Controller.LoadAllIngredientLists();
        //m_Controller.LoadStyles();
    }

    private void SetupTabChangeListener() {
        final Activity a = this;

        m_TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Tools.hideKeyboard(a);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void FindUIElements() {


        m_TabLayout = findViewById(R.id.recipeEditorTabs);
        m_ViewPager = findViewById(R.id.recipeViewPager);

        appBarLayout = findViewById(R.id.appbar);
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
            case R.id.startBrewLog:
                m_Controller.startNewBrewLog();
                break;
            case R.id.changeStyle:
                m_Controller.showChangeStylePrompt();
                break;
            case R.id.deleteRecipe:
                m_Controller.AskDeleteRecipe();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void FinishActivity(String sIDString, double dAbv, boolean bDeleted, String sStyleName) {
        Intent i = new Intent();
        i.putExtra("idString", sIDString);
        i.putExtra("abv", dAbv);
        i.putExtra("deleted", bDeleted);
        i.putExtra("styleName", sStyleName);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onBackPressed() {
        m_Controller.GoBack();

        super.onBackPressed();
    }

    @Override
    public void PromptDeletion() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //yes
                        m_Controller.DeleteRecipe();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //no
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete? Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }



    @Override
    public void onPause() {
        super.onPause();

        m_Controller.SaveRecipe();
    }

    @Override
    public void SetTitleSRMColor(int iColor, int iDarkColor) {
        appBarLayout.setBackgroundColor(iColor);
        Window window = this.getWindow();
        window.setStatusBarColor(iDarkColor);
    }

    @Override
    public void launchBrewLogActivity(String idString) {
        Intent i = new Intent(getApplicationContext(), ViewBrewLog.class);
        Bundle b = new Bundle();
        //b.putSerializable("brewLog", brewLog);
        b.putString("idString", idString);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void PopulateBrewLogList(BrewLog[] brewLogs) {
        recipeEditorPagerAdapter.PopulateBrewLogList(brewLogs);
    }
}
