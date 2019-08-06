package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import beer.unaccpetable.brewzilla.Fragments.BrewLogList.BrewLogList;
import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashFragment;
import beer.unaccpetable.brewzilla.Fragments.MashSetup.MashSetupController;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeFragment;
import beer.unaccpetable.brewzilla.Fragments.RecipeView.RecipeViewController;
import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditorController;

public class RecipeEditorPagerAdapter extends FragmentStatePagerAdapter {

    private RecipeFragment m_fRecipe;
    private BrewLogList m_fBrewLogs;


    public RecipeEditorPagerAdapter(FragmentManager fm, RecipeViewController recipeViewController) {
        super(fm);

        m_fRecipe = RecipeFragment.newInstance();
        m_fRecipe.setController(recipeViewController);

        m_fBrewLogs = BrewLogList.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return m_fRecipe;
        } else if (position == 1) {
            return m_fBrewLogs;
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
                return "BREW LOGS";
            /*case 2:
                return "YEASTS";*/
        }
        return null;
    }

    public void PopulateBrewLogList(BrewLog[] brewLogs) {
        m_fBrewLogs.PopulateBrewLogList(brewLogs);
    }
}
