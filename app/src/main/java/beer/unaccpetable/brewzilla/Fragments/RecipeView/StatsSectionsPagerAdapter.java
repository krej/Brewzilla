package beer.unaccpetable.brewzilla.Fragments.RecipeView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Style;

public class StatsSectionsPagerAdapter extends FragmentStatePagerAdapter {
    private RecipeStatsDataFragment m_oDataFragment;
    private RecipeStatsGraphFragment m_oGraphFragment;

    public StatsSectionsPagerAdapter(FragmentManager fm) {
        super(fm);

        m_oDataFragment = RecipeStatsDataFragment.newInstance();
        m_oGraphFragment = RecipeStatsGraphFragment.newInstance();
    }


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
        m_oDataFragment.setStats(stats);
        m_oGraphFragment.setStats(stats);
    }

    public void PopulateStyleRanges(Style style) {
        m_oDataFragment.setStyle(style);
        m_oGraphFragment.setStyle(style);
    }
}
