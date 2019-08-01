package beer.unaccpetable.brewzilla.Screens.RecipeEditor.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditorController;

public class RecipeEditorPagerAdapter extends FragmentStatePagerAdapter {

    RecipeFragment m_fRecipe;
    MashFragment m_fMash;


    public RecipeEditorPagerAdapter(FragmentManager fm, RecipeEditorController controller) {
        super(fm);

        m_fRecipe = RecipeFragment.newInstance();
        m_fMash = MashFragment.newInstance();

        m_fRecipe.setController(controller);
        m_fMash.setController(controller);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return m_fRecipe;
        } else if (position == 1) {
            if (m_fMash == null)
                m_fMash = MashFragment.newInstance();
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
