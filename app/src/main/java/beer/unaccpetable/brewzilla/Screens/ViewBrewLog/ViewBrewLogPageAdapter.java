package beer.unaccpetable.brewzilla.Screens.ViewBrewLog;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import beer.unaccpetable.brewzilla.Fragments.BrewStats.BrewStatsController;
import beer.unaccpetable.brewzilla.Fragments.BrewStats.BrewStatsFragment;
import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashFragment;
import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashSetupController;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeFragment;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeViewController;

public class ViewBrewLogPageAdapter extends FragmentStatePagerAdapter {
    //private RecipeFragment m_fOriginalRecipe;
    private RecipeFragment m_fRectifiedRecipe;
    private MashFragment m_fMash;
    private BrewStatsFragment m_fBrewStats;

    ViewBrewLogPageAdapter(FragmentManager fm,
                           RecipeViewController originalController,
                           RecipeViewController rectifiedController,
                           MashSetupController mashController,
                           BrewStatsController brewStatsController) {
        super(fm);
/*

        m_fOriginalRecipe = RecipeFragment.newInstance();
        m_fOriginalRecipe.setController(originalController);
*/

        m_fRectifiedRecipe = RecipeFragment.newInstance();
        m_fRectifiedRecipe.setController(rectifiedController);

        m_fMash = MashFragment.newInstance();
        m_fMash.setController(mashController);

        m_fBrewStats = BrewStatsFragment.newInstance();
        m_fBrewStats.setController(brewStatsController);


    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return m_fRectifiedRecipe;
            case 1:
                return m_fMash;
            case 2:
                return m_fBrewStats;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "RECIPE";
            case 1:
                return "MASH";
            case 2:
                return "STATS";
        }
        return null;
    }

}
