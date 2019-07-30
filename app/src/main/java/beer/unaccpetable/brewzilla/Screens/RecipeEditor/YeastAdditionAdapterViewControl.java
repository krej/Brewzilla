package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Models.YeastAddition;
import beer.unaccpetable.brewzilla.R;

public class YeastAdditionAdapterViewControl extends BaseAdapterViewControl {
    private ArrayList<Yeast> m_Yeasts;
    private RecipeEditorController m_Controller;

    public YeastAdditionAdapterViewControl(RecipeEditorController controller) {
        m_Controller = controller;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {
        /*final Yeast h = (Yeast) i;

        final Spinner snName = (Spinner) root.findViewById(R.id.yeastSelector);
        final EditText lab = (EditText) root.findViewById(R.id.lab);
        final EditText att = (EditText) root.findViewById(R.id.attenuation);
        final EditText yeastID = (EditText) root.findViewById(R.id.yeastID);

        Tools.PopulateDropDown(snName, root.getContext(), m_Yeasts.toArray());
        if (i != null)
            Tools.SetDropDownSelection(snName, m_Yeasts.toArray(), h);

        snName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Yeast h = (Yeast)parent.getItemAtPosition(position);
                if (h.name != "Select a grain") {

                    lab.setText(String.valueOf(h.lab));
                    att.setText(String.valueOf(h.attenuation));
                    yeastID.setText(h.idString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (i != null) {

            lab.setText(h.lab);
            att.setText(String.valueOf(h.attenuation));
        }*/
    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        YeastAddition y = (YeastAddition) i;

        /*view.txtHeader.setText(y.name);
        view.txtFooter.setText("Lab: " + y.lab);
        view.txtThirdLine.setText("Attenuation: " + y.attenuation);*/

        TextView tvLab = view.view.findViewById(R.id.yeastLab);
        TextView tvName = view.view.findViewById(R.id.yeastName);
        TextView tvAttenuation = view.view.findViewById(R.id.attenuation);

        Tools.SetText(tvLab, y.yeast.lab);
        Tools.SetText(tvName, y.yeast.name);
        Tools.SetText(tvAttenuation, "Attenuation: " + y.yeast.attenuation);

    }

    @Override
    public void onItemClick(View v, ListableObject i) {
        //m_Adapter.showAddItemDialog(v.getContext(), i);
        RelativeLayout rlHidden = v.findViewById(R.id.rlYeastHidden);
        if (rlHidden.getVisibility() == View.GONE) {
            rlHidden.setVisibility(View.VISIBLE);
        } else {
            rlHidden.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemLongPress(View v, ListableObject i) {

    }

    @Override
    public boolean onDialogOkClicked(Dialog d, ListableObject i) {
        /*boolean bExisting = i != null;

        final Spinner snName = (Spinner) d.findViewById(R.id.yeastSelector);
        EditText lab = (EditText) d.findViewById(R.id.lab);
        EditText att = (EditText) d.findViewById(R.id.attenuation);
        EditText txtYeastID = (EditText) d.findViewById(R.id.yeastID);

        Yeast f = (Yeast) snName.getSelectedItem();

        String sName = f.name;
        String sLab = lab.getText().toString();
        double dAtt = Tools.ParseDouble(att.getText().toString());
        String yeastID = txtYeastID.getText().toString();

        if (sName.length() == 0 || sLab.length() == 0 || dAtt == 0) {
            //InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            Yeast y = (Yeast) i;
            y.name = sName;
            y.lab = sLab;
            y.attenuation = dAtt;
            y.idString = yeastID;
            //y.recipeID = sExtraInfo;
        } else {
            Yeast yeast = new Yeast(sName, sLab, dAtt);
            yeast.idString = yeastID;
            //yeast.recipeID = sExtraInfo;
            m_Adapter.add(yeast);
        }

        m_Controller.SaveRecipe();*/
        return true;
    }

    public void PopulateList(ArrayList<Yeast> yeasts) {
        m_Yeasts = yeasts;
    }
}
