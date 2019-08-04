package beer.unaccpetable.brewzilla.Fragments.RecipeView;

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
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditorController;

public class HopAdditionAdapterViewControl extends BaseAdapterViewControl {
    private ArrayList<Hop> m_Hops;
    private RecipeViewController m_Controller;

    public HopAdditionAdapterViewControl(RecipeViewController controller) {
        m_Controller = controller;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {

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

        etTime.addTextChangedListener(tw);
        etTime.setOnFocusChangeListener(m_Controller.createFocusChangeListener());
        etAmount.addTextChangedListener(tw);
        etAmount.setOnFocusChangeListener(m_Controller.createFocusChangeListener());
        etAAU.addTextChangedListener(tw);
        etAAU.setOnFocusChangeListener(m_Controller.createFocusChangeListener());

        spType.setOnItemSelectedListener(new CustomOnItemSelectedListener(new CustomOnItemSelectedListener.IMyAdapterViewOnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChangeHopAddition(h, etTime, etAmount, etAAU, spType);
                SetHopTypeLabel(h, tvTimeLabel);
                //m_Controller.SaveRecipe();
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
