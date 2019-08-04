package beer.unaccpetable.brewzilla.Fragments.RecipeView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.Controls.StyleRangeBar;

public class RecipeStatsGraphFragment extends Fragment {

    StyleRangeBar m_srbIBU, m_srbOG, m_srbFG, m_srbABV, m_srbSRM;

    public static RecipeStatsGraphFragment newInstance() {
        RecipeStatsGraphFragment fragmentFirst = new RecipeStatsGraphFragment();
        Bundle args = new Bundle();
        /*args.putInt("someInt", page);
        args.putString("someTitle", title);*/
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);
        //title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_stats_graphs, container, false);

        m_srbIBU = view.findViewById(R.id.ibuBar);
        m_srbOG = view.findViewById(R.id.ogBar);
        m_srbFG = view.findViewById(R.id.fgBar);
        m_srbABV = view.findViewById(R.id.abvBar);
        m_srbSRM = view.findViewById(R.id.srmBar);

        return view;
    }

    public void PopulateStats(RecipeStatistics stats) {

        m_srbIBU.setValue(stats.ibu);
        m_srbOG.setValue(stats.getOgPoints());
        m_srbFG.setValue(stats.getFgPoints());
        m_srbABV.setValue(stats.abv);
        m_srbSRM.setValue(stats.srm);
    }

    public void PopulateStyleRanges(Style style) {

        m_srbIBU.setRange(style.minIBU, style.maxIBU);
        m_srbOG.setRange(Tools.DecimalPart(style.minOG), Tools.DecimalPart(style.maxOG));
        m_srbFG.setRange(Tools.DecimalPart(style.minFG), Tools.DecimalPart(style.maxFG));
        m_srbABV.setRange(style.minABV, style.maxABV);
        m_srbSRM.setRange(style.minColor, style.maxColor);
    }
}
