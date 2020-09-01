package beer.unaccpetable.brewzilla.Fragments.RecipeView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.R;

public class RecipeStatsDataFragment extends Fragment {
    TextView m_tvIBUActual, m_tvIBUMin, m_tvIBUMax;
    TextView m_tvOGActual, m_tvOGMin, m_tvOGMax;
    TextView m_tvFGActual, m_tvFGMin, m_tvFGMax;
    TextView m_tvABVActual, m_tvABVMin, m_tvABVMax;
    TextView m_tvSRMActual, m_tvSRMMin, m_tvSRMMax;

    private RecipeStatistics m_stats;
    private Style m_Style;


    private boolean m_bStatsPopulated, m_bStylePopulated;
    private boolean m_bOnCreateViewRan;

    public static RecipeStatsDataFragment newInstance() {
        RecipeStatsDataFragment fragmentFirst = new RecipeStatsDataFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_stats_data, container, false);


        m_tvIBUActual = view.findViewById(R.id.ibuDataActual);
        m_tvIBUMin = view.findViewById(R.id.ibuDataMin);
        m_tvIBUMax = view.findViewById(R.id.ibuDataMax);

        m_tvOGActual = view.findViewById(R.id.ogDataActual);
        m_tvOGMin = view.findViewById(R.id.ogDataMin);
        m_tvOGMax = view.findViewById(R.id.ogDataMax);

        m_tvFGActual = view.findViewById(R.id.fgDataActual);
        m_tvFGMin = view.findViewById(R.id.fgDataMin);
        m_tvFGMax = view.findViewById(R.id.fgDataMax);

        m_tvABVActual = view.findViewById(R.id.abvDataActual);
        m_tvABVMin = view.findViewById(R.id.abvDataMin);
        m_tvABVMax = view.findViewById(R.id.abvDataMax);

        m_tvSRMActual = view.findViewById(R.id.srmDataActual);
        m_tvSRMMin = view.findViewById(R.id.srmDataMin);
        m_tvSRMMax = view.findViewById(R.id.srmDataMax);

        m_bOnCreateViewRan = true;

        if (m_stats != null)
            PopulateStats(m_stats);

        if (m_Style != null)
            PopulateStyleRanges(m_Style);



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);

        /*if (m_stats != null)
            PopulateStats(m_stats);

        if (m_Style != null)
            PopulateStyleRanges(m_Style);*/
    }

    public void PopulateStats(RecipeStatistics stats) {
        Tools.SetText(m_tvIBUActual, Tools.RoundString(stats.ibu, 2));
        Tools.SetText(m_tvOGActual, Tools.RoundString(stats.og, 3));
        Tools.SetText(m_tvFGActual, Tools.RoundString(stats.fg, 3));
        Tools.SetText(m_tvABVActual, Tools.RoundString(stats.abv, 3));
        Tools.SetText(m_tvSRMActual, Tools.RoundString(stats.srm, 2));

        m_bStatsPopulated = true;
    }

    public void PopulateStyleRanges(Style style) {
        Tools.SetText(m_tvIBUMin, Tools.RoundString(style.minIBU, 2));
        Tools.SetText(m_tvIBUMax, Tools.RoundString(style.maxIBU, 2));

        Tools.SetText(m_tvOGMin, Tools.RoundString(style.minOG, 3));
        Tools.SetText(m_tvOGMax, Tools.RoundString(style.maxOG, 3));

        Tools.SetText(m_tvFGMin, Tools.RoundString(style.minFG, 3));
        Tools.SetText(m_tvFGMax, Tools.RoundString(style.maxFG, 3));

        Tools.SetText(m_tvABVMin, Tools.RoundString(style.minABV, 3));
        Tools.SetText(m_tvABVMax, Tools.RoundString(style.maxABV, 3));

        Tools.SetText(m_tvSRMMin, Tools.RoundString(style.minColor, 2));
        Tools.SetText(m_tvSRMMax, Tools.RoundString(style.maxColor, 2));

        m_bStylePopulated = true;
    }

    public void setStats(RecipeStatistics stats) {
        m_stats = stats;

        if (!m_bStatsPopulated && m_bOnCreateViewRan)
            PopulateStats(stats);
    }

    public void setStyle(Style style) {
        m_Style = style;

        if (!m_bStylePopulated && m_bOnCreateViewRan)
            PopulateStyleRanges(style);
    }
}
