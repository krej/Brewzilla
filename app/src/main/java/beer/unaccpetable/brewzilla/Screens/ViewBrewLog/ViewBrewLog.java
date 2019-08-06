package beer.unaccpetable.brewzilla.Screens.ViewBrewLog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Repositories.Repository;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditorPagerAdapter;

public class ViewBrewLog extends AppCompatActivity implements ViewBrewLogController.View {

    TabLayout m_TabLayout;
    ViewPager m_ViewPager;

    ViewBrewLogController m_Controller;

    TextView m_tvBrewLogBeerName;
    TextView m_tvBrewLogName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_log);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String idString = null;
        if (bundle != null) {
            idString = bundle.getString("idString");
        }

        m_Controller = new ViewBrewLogController(new Repository(), this);

        FindUIElements();

        ViewBrewLogPageAdapter recipeEditorPagerAdapter = new ViewBrewLogPageAdapter(getSupportFragmentManager(), m_Controller.getOriginalController(), m_Controller.getRectifiedController(), m_Controller.getMashController());
        m_ViewPager.setAdapter(recipeEditorPagerAdapter);
        m_TabLayout.setupWithViewPager(m_ViewPager);

        m_Controller.LoadBrewLog(idString);
    }

    private void FindUIElements() {
        m_TabLayout = findViewById(R.id.brewLogTabs);
        m_ViewPager = findViewById(R.id.brewLogViewPager);
        m_tvBrewLogBeerName = findViewById(R.id.brewLogBeerName);
        m_tvBrewLogName = findViewById(R.id.brewLogName);
    }

    @Override
    public void ShowToast(String sMessage) {
        Tools.ShowToast(getApplicationContext(), sMessage, Toast.LENGTH_SHORT);
    }

    @Override
    public void SetScreenTitle(String sBeerName, String sBrewLogName) {
        Tools.SetText(m_tvBrewLogBeerName, sBeerName);
        Tools.SetText(m_tvBrewLogName, sBrewLogName);
    }

}
