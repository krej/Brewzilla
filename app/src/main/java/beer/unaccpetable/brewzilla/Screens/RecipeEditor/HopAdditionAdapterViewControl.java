package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Controls.CustomOnItemSelectedListener;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.ArrayList;
import java.util.Arrays;

import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;
import beer.unaccpetable.brewzilla.R;

public class HopAdditionAdapterViewControl extends BaseAdapterViewControl {
    private ArrayList<Hop> m_Hops;
    private RecipeEditorController m_Controller;

    public HopAdditionAdapterViewControl(RecipeEditorController controller) {
        m_Controller = controller;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {
        /*final HopAddition h = (HopAddition) i;

        final Spinner snName = (Spinner) root.findViewById(R.id.hopSelector);
        final Spinner snType = (Spinner) root.findViewById(R.id.hopTypeSpinner);

        final EditText hopID = (EditText) root.findViewById(R.id.hopID);
        final EditText amount = (EditText) root.findViewById(R.id.amount);
        final EditText aau = (EditText) root.findViewById(R.id.aau);
        final EditText time = (EditText) root.findViewById(R.id.time);

        final Context c = root.getContext();

        Tools.PopulateDropDown(snName, root.getContext(), m_Hops.toArray());
        if (i != null)
            Tools.SetDropDownSelection(snName, m_Hops.toArray(), h.hop);


        snName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Hop h = (Hop)parent.getItemAtPosition(position);
                if (h.name != "Select a hop") {

                    aau.setText(String.valueOf(h.aau));
                    hopID.setText(h.idString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, HopAddition.Types);
        snType.setAdapter(typeAdapter);

        if (i != null) {

            hopID.setText(h.hop.idString);
            amount.setText(String.valueOf(h.amount));
            aau.setText(String.valueOf(h.hop.aau));
            time.setText(String.valueOf(h.time));
            snType.setSelection(Arrays.asList(HopAddition.Types).indexOf(h.type));
        }
*/
    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        HopAddition h = (HopAddition) i;
        /*view.txtHeader.setText(h.hop.name);
        view.txtFooter.setText("Time: " + h.time + " min");
        view.txtThirdLine.setText(h.amount + " oz");
        view.txtFourthLine.setText(h.hop.aau + " AAU");*/

        TextView tvName = view.view.findViewById(R.id.hopName);
        EditText etTime = view.view.findViewById(R.id.hopTime);
        TextView tvTimeLabel = view.view.findViewById(R.id.hopTimeLabel);
        EditText etAmount = view.view.findViewById(R.id.hopAmount);
        Spinner spType = view.view.findViewById(R.id.spHopType);
        EditText etAAU = view.view.findViewById(R.id.hopAAU);

        Tools.SetText(tvName, h.hop.name);
        Tools.SetText(etTime, h.time);
        SetHopTypeLabel(h, tvTimeLabel);
        Tools.SetText(etAmount, h.amount);
        Tools.SetText(etAAU, h.hop.aau);

        Tools.PopulateDropDown(spType, view.view.getContext(), HopAddition.Types);
        Tools.SetDropDownSelection(spType, HopAddition.Types, h.type);


        //setup listeners
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ChangeHopAddition(h, etTime, etAmount, etAAU, spType);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    m_Controller.SaveRecipe();
                }
            }
        };

        etTime.addTextChangedListener(tw);
        etTime.setOnFocusChangeListener(focusChangeListener);
        etAmount.addTextChangedListener(tw);
        etAmount.setOnFocusChangeListener(focusChangeListener);
        etAAU.addTextChangedListener(tw);
        etAAU.setOnFocusChangeListener(focusChangeListener);

        spType.setOnItemSelectedListener(new CustomOnItemSelectedListener(new CustomOnItemSelectedListener.IMyAdapterViewOnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChangeHopAddition(h, etTime, etAmount, etAAU, spType);
                SetHopTypeLabel(h, tvTimeLabel);
                m_Controller.SaveRecipe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));
    }

    private void SetHopTypeLabel(HopAddition h, TextView tvTimeLabel) {
        Tools.SetText(tvTimeLabel, h.type.equals("Dry Hop") ? "Days" : "Min");
    }

    private void ChangeHopAddition(HopAddition h, EditText etTime, EditText etAmount, EditText etAAU, Spinner spType) {
        HopAddition ha = m_Controller.hopChanged(h, etTime.getText().toString(), etAmount.getText().toString(), etAAU.getText().toString(), (String)spType.getSelectedItem());
        h.type = ha.type;
        h.amount = ha.amount;
        h.time = ha.time;
        h.hop.aau = ha.hop.aau;
    }

    @Override
    public void onItemClick(View v, ListableObject i) {
        //m_Adapter.showAddItemDialog(v.getContext(), i);
        RelativeLayout rlHidden = v.findViewById(R.id.rlHopHidden);
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
        /*Spinner name = (Spinner) d.findViewById(R.id.hopSelector);
        Spinner snType = (Spinner) d.findViewById(R.id.hopTypeSpinner);
        EditText amount = (EditText) d.findViewById(R.id.amount);
        EditText aau = (EditText) d.findViewById(R.id.aau);
        EditText time = (EditText) d.findViewById(R.id.time);

        boolean bExisting = i != null;
        Hop h = (Hop)name.getSelectedItem();

        String sName = h.name;
        double dAmount = Tools.ParseDouble(amount.getText().toString());
        double dAAU = Tools.ParseDouble(aau.getText().toString());
        int iTime = Tools.ParseInt(time.getText().toString());
        String sType = snType.getSelectedItem().toString();

        if (sName.equals("Select a hop") || dAmount == 0 || iTime == 0) {
            //InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            HopAddition hopAddition =(HopAddition) i;
            hopAddition.hop.name = sName;
            hopAddition.amount = dAmount;
            hopAddition.hop.aau = dAAU;
            hopAddition.time = iTime;
            //hopAddition.recipeID = sExtraInfo;
            hopAddition.hopID = h.idString;
            hopAddition.type = sType;
        } else {
            HopAddition hop = new HopAddition(sName, dAmount, dAAU, iTime);
            //hop.recipeID = sExtraInfo;
            hop.hopID = h.idString;
            hop.type = sType;
            m_Adapter.add(hop);
        }

        m_Controller.SaveRecipe();*/
        return true;
    }

    public void PopulateList(ArrayList<Hop> hops) {
        m_Hops = hops;
    }

    @Override
    public void setReadOnly(NewAdapter.ViewHolder view, boolean bReadOnly) {
        EditText etTime = view.view.findViewById(R.id.hopTime);
        EditText etAmount = view.view.findViewById(R.id.hopAmount);
        Spinner spType = view.view.findViewById(R.id.spHopType);
        EditText etAAU = view.view.findViewById(R.id.hopAAU);

        etTime.setEnabled(!bReadOnly);
        etAmount.setEnabled(!bReadOnly);
        spType.setEnabled(!bReadOnly);
        etAAU.setEnabled(!bReadOnly);
    }
}
