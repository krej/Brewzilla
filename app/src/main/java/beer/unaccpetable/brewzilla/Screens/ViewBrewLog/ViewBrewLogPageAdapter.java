package beer.unaccpetable.brewzilla.Screens.ViewBrewLog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashFragment;
import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashSetupController;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeFragment;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeViewController;

public class ViewBrewLogPageAdapter extends FragmentStatePagerAdapter {
    //private RecipeFragment m_fOriginalRecipe;
    private RecipeFragment m_fRectifiedRecipe;
    private MashFragment m_fMash;

    ViewBrewLogPageAdapter(FragmentManager fm, RecipeViewController originalController, RecipeViewController rectifiedController, MashSetupController mashController) {
        super(fm);
/*

        m_fOriginalRecipe = RecipeFragment.newInstance();
        m_fOriginalRecipe.setController(originalController);
*/

        m_fRectifiedRecipe = RecipeFragment.newInstance();
        m_fRectifiedRecipe.setController(rectifiedController);

        m_fMash = MashFragment.newInstance();
        m_fMash.setController(mashController);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return m_fRectifiedRecipe;
            case 1:
                return m_fMash;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "RECIPE";
            case 1:
                return "MASH";
            /*case 2:
                return "YEASTS";*/
        }
        return null;
    }

}
