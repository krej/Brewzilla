package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

import beer.unaccpetable.brewzilla.Adapters.SearchDialogAdapterViewControl;
import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;

import com.unacceptable.unacceptablelibrary.Controls.CustomOnItemSelectedListener;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeParameters;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Models.YeastAddition;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;
import beer.unaccpetable.brewzilla.Screens.BaseActivity;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.Fragments.RecipeEditorPagerAdapter;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.Fragments.StatsSectionsPagerAdapter;
import beer.unaccpetable.brewzilla.Tools.Calculations;
import beer.unaccpetable.brewzilla.Tools.Controls.StyleRangeBar;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

public class RecipeEditor extends BaseActivity implements RecipeEditorController.IActivityView {

    private RecipeEditorController m_Controller;

    Toolbar toolbar;
    AppBarLayout appBarLayout;

    TabLayout m_TabLayout;
    ViewPager m_ViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FindUIElements();

        m_Controller = new RecipeEditorController(new Repository());
        m_Controller.attachActivityView(this);

        RecipeEditorPagerAdapter recipeEditorPagerAdapter = new RecipeEditorPagerAdapter(getSupportFragmentManager(), m_Controller);
        m_ViewPager.setAdapter(recipeEditorPagerAdapter);
        m_TabLayout.setupWithViewPager(m_ViewPager);

        SetupTabChangeListener();

        String sID = getIntent().getStringExtra("RecipeID");
        m_Controller.LoadRecipe(sID);
        m_Controller.LoadIngredientLists();
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
    public void FinishActivity(String sIDString, double dAbv, boolean bDeleted) {
        Intent i = new Intent();
        i.putExtra("idString", sIDString);
        i.putExtra("abv", dAbv);
        i.putExtra("deleted", bDeleted);
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
}
