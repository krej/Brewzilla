package beer.unaccpetable.brewzilla.Screens.RecipeEditor.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Screens.IngredientManager;

public class StatsSectionsPagerAdapter extends FragmentPagerAdapter {
    public StatsSectionsPagerAdapter(FragmentManager fm) {
        super(fm);

        m_oDataFragment = RecipeStatsDataFragment.newInstance();
        m_oGraphFragment = RecipeStatsGraphFragment.newInstance();
    }

    private RecipeStatsDataFragment m_oDataFragment;
    private RecipeStatsGraphFragment m_oGraphFragment;

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            if (m_oDataFragment == null)
                m_oDataFragment = RecipeStatsDataFragment.newInstance();
            return m_oDataFragment;
        } else if (position == 1) {
            if (m_oGraphFragment == null)
                m_oGraphFragment = RecipeStatsGraphFragment.newInstance();
            return m_oGraphFragment;
        }

        return null;

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "";
            case 1:
                return "";
        }
        return null;
    }

    public void PopulateStats(RecipeStatistics stats) {
        m_oDataFragment.PopulateStats(stats);
        m_oGraphFragment.PopulateStats(stats);
    }

    public void PopulateStyleRanges(Style style) {
        m_oDataFragment.PopulateStyleRanges(style);
        m_oGraphFragment.PopulateStyleRanges(style);
    }
}
