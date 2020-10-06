package beer.unaccpetable.brewzilla.Screens.IngredientManager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import beer.unaccpetable.brewzilla.Models.Adjunct;
import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls.AdjunctAdapterViewControl;
import beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls.FermentableAdapterViewControl;
import beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls.HopAdapterViewControl;
import beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls.YeastAdapterViewControl;

public class IngredientManagerPagerAdapter
        extends FragmentPagerAdapter
{
    private IngredientListFragment<Hop> m_fHops;
    private IngredientListFragment<Fermentable> m_fFermentables;
    private IngredientListFragment<Yeast> m_fYeasts;
    private IngredientListFragment<Adjunct> m_fAdjuncts;

    public IngredientManagerPagerAdapter(FragmentManager fm) {
        super(fm);

        m_fHops = IngredientListFragment.newInstance();
        m_fFermentables = IngredientListFragment.newInstance();
        m_fYeasts = IngredientListFragment.newInstance();
        m_fAdjuncts = IngredientListFragment.newInstance();

        m_fHops.setDialogLayout(R.layout.fragment_hop_dialog);
        m_fFermentables.setDialogLayout(R.layout.fragment_malt_dialog);
        m_fYeasts.setDialogLayout(R.layout.fragment_yeast_dialog);
        m_fAdjuncts.setDialogLayout(R.layout.fragment_adjunct_dialog);

        m_fHops.setAdapterViewControl(new HopAdapterViewControl());
        m_fFermentables.setAdapterViewControl(new FermentableAdapterViewControl());
        m_fYeasts.setAdapterViewControl(new YeastAdapterViewControl());
        m_fAdjuncts.setAdapterViewControl(new AdjunctAdapterViewControl());

        m_fHops.setIngredientCollection("hop");
        m_fFermentables.setIngredientCollection("fermentable");
        m_fYeasts.setIngredientCollection("yeast");
        m_fAdjuncts.setIngredientCollection("adjunct");
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return m_fHops;
        } else if (position == 1) {
            return m_fFermentables;
        } else if (position == 2 ) {
            return m_fYeasts;
        } else if (position == 3) {
            return m_fAdjuncts;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
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
            case 3:
                return "ADJUNCTS";
        }
        return null;
    }

    public void addNewItem(int iPosition) {
        switch (iPosition) {
            case 0:
                m_fHops.addNewItem();
                break;
            case 1:
                m_fFermentables.addNewItem();
                break;
            case 2:
                m_fYeasts.addNewItem();
                break;
            case 3:
                m_fAdjuncts.addNewItem();
                break;
        }
    }

}
