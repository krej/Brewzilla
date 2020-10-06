package beer.unaccpetable.brewzilla.Fragments.BrewLogList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Adapters.SimpleAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Screens.ViewBrewLog.ViewBrewLog;

public class BrewLogList extends Fragment {

    RecyclerView m_lstBrewLogs;
    NewAdapter m_Adapter;

    public static BrewLogList newInstance() {
        BrewLogList fragmentFirst = new BrewLogList();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.simple_recycler_view, container, false);

        m_lstBrewLogs = view.findViewById(R.id.simpleRecyclerView);

        m_Adapter = Tools.setupRecyclerView(m_lstBrewLogs, getContext(), R.layout.one_line_list, 0, false, new SimpleAdapterViewControl() {
            @Override
            public void SetupDialog(View root, ListableObject i) {

            }

            @Override
            public void onItemClick(View v, ListableObject i) {
                Intent intent = new Intent(getContext(), ViewBrewLog.class);
                Bundle b = new Bundle();
                b.putString("idString", i.idString);
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onItemLongPress(View v, ListableObject i) {

            }

            @Override
            public boolean onDialogOkClicked(Dialog d, ListableObject i) {
                return false;
            }
        }, true, true);

        return view;
    }

    public void PopulateBrewLogList(BrewLog[] brewLogs) {
        for (BrewLog brewLog : brewLogs) {
            m_Adapter.add(brewLog);
        }
    }
}
