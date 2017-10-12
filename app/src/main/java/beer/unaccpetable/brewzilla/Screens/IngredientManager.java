package beer.unaccpetable.brewzilla.Screens;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beer.unaccpetable.brewzilla.Adapters.FermentableAdapter;
import beer.unaccpetable.brewzilla.Adapters.HopAdapter;
import beer.unaccpetable.brewzilla.Adapters.YeastAdapter;
import beer.unaccpetable.brewzilla.Ingredients.Fermentable;
import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Yeast;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.Network;
import beer.unaccpetable.brewzilla.Tools.Tools;

public class IngredientManager extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                int position = mViewPager.getCurrentItem();
                switch (position) {
                    case 0:
                        HopsFragment hf = (HopsFragment)mSectionsPagerAdapter.getItem(position);

                        hf.AddHop();
                        break;
                    case 1:
                        FermentablesFragment ff = (FermentablesFragment)mSectionsPagerAdapter.getItem(position);
                        ff.AddHop();
                        break;
                    case 2:
                        YeastFragment yf = (YeastFragment)mSectionsPagerAdapter.getItem(position);
                        yf.AddHop();
                        break;
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredient_manager, menu);
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

    //TODO: Try to make this 1 class instead of 3. Also try to figure out how to not clear the data every time it loads it. Why is it always reloading data?

    public static class HopsFragment extends Fragment {
        private RecyclerView.LayoutManager m_LayoutManager;
        private RecyclerView lstHops;
        private HopAdapter m_HopAdapter = new HopAdapter(R.layout.recipe_list, R.layout.fragment_hop_dialog);

        public HopsFragment() {}

        public static HopsFragment newInstance(int sectionNumber) {
            HopsFragment fragment = new HopsFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ingredient_manager, container, false);

            lstHops = (RecyclerView) rootView.findViewById(R.id.hopManager);
            lstHops.setHasFixedSize(false);
            m_LayoutManager = new LinearLayoutManager(container.getContext());
            lstHops.setLayoutManager(m_LayoutManager);
            lstHops.setAdapter(m_HopAdapter);

            LoadHops();

            return rootView;
        }

        private void LoadHops() {
            m_HopAdapter.clear();
            Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/hop", null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            Hop[] hops = gson.fromJson(response, Hop[].class);

                            for (Hop h : hops) {
                                m_HopAdapter.add(h);
                            }
                        }
                    }, null);
        }

        public void AddHop() {
            Context c = lstHops.getContext();
            m_HopAdapter.AddItem(c, null);
        }
    }

    public static class FermentablesFragment extends Fragment {
        private RecyclerView.LayoutManager m_LayoutManager;
        private RecyclerView lstHops;
        private FermentableAdapter m_HopAdapter = new FermentableAdapter(R.layout.recipe_list, R.layout.fragment_malt_dialog);

        public FermentablesFragment() {}

        public static FermentablesFragment newInstance(int sectionNumber) {
            FermentablesFragment fragment = new FermentablesFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ingredient_manager, container, false);

            lstHops = (RecyclerView) rootView.findViewById(R.id.hopManager);
            lstHops.setHasFixedSize(false);
            m_LayoutManager = new LinearLayoutManager(container.getContext());
            lstHops.setLayoutManager(m_LayoutManager);
            lstHops.setAdapter(m_HopAdapter);

            LoadHops();

            return rootView;
        }

        private void LoadHops() {
            m_HopAdapter.clear();
            Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/fermentable", null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            Fermentable[] hops = gson.fromJson(response, Fermentable[].class);

                            for (Fermentable h : hops) {
                                m_HopAdapter.add(h);
                            }
                        }
                    }, null);
        }

        public void AddHop() {
            Context c = lstHops.getContext();
            m_HopAdapter.AddItem(c, null);
        }
    }

    public static class YeastFragment extends Fragment {
        private RecyclerView.LayoutManager m_LayoutManager;
        private RecyclerView lstHops;
        private YeastAdapter m_HopAdapter = new YeastAdapter(R.layout.recipe_list, R.layout.fragment_yeast_dialog);

        public YeastFragment() {}

        public static YeastFragment newInstance(int sectionNumber) {
            YeastFragment fragment = new YeastFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ingredient_manager, container, false);

            lstHops = (RecyclerView) rootView.findViewById(R.id.hopManager);
            lstHops.setHasFixedSize(false);
            m_LayoutManager = new LinearLayoutManager(container.getContext());
            lstHops.setLayoutManager(m_LayoutManager);
            lstHops.setAdapter(m_HopAdapter);

            LoadHops();

            return rootView;
        }

        private void LoadHops() {
            m_HopAdapter.clear();

            Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/yeast", null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            Yeast[] hops = gson.fromJson(response, Yeast[].class);

                            for (Yeast h : hops) {
                                m_HopAdapter.add(h);
                            }
                        }
                    }, null);
        }

        public void AddHop() {
            Context c = lstHops.getContext();
            m_HopAdapter.AddItem(c, null);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private HopsFragment m_oHopsFragment;
        private FermentablesFragment m_oFermentablesFragment;
        private YeastFragment m_oYeastFragment;

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                if (m_oHopsFragment == null)
                    m_oHopsFragment = HopsFragment.newInstance(1);
                return m_oHopsFragment;
            } else if (position == 1) {
                if (m_oFermentablesFragment == null)
                    m_oFermentablesFragment = FermentablesFragment.newInstance(1);
                return m_oFermentablesFragment;
            } else if (position == 2 ) {
                if(m_oYeastFragment == null)
                    m_oYeastFragment = YeastFragment.newInstance(1);
                return m_oYeastFragment;
            }

            return null;
            /*
            Fragment f = null;
            switch (position) {
                case 0:
                if (m_oHopsFragment == null)
                    m_oHopsFragment = HopsFragment.newInstance(position + 1);
                //return m_oHopsFragment;
                //return HopsFragment.newInstance(position + 1);
                f = m_oHopsFragment;
                default:
                    f = PlaceholderFragment.newInstance(position + 1);
            }
            return f;
            //return PlaceholderFragment.newInstance(position + 1);
*/
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOPS";
                case 1:
                    return "FERMENTABLES";
                case 2:
                    return "YEASTS";
            }
            return null;
        }
    }
}
