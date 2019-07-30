package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.R;

public class FermentableAdditionAdapterViewControl extends BaseAdapterViewControl {
    private ArrayList<Fermentable> m_Fermentables;
    private RecipeEditorController m_Controller;

    public FermentableAdditionAdapterViewControl(RecipeEditorController controller) {
        m_Controller = controller;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {
        /*final FermentableAddition h = (FermentableAddition) i;
        final EditText weight = (EditText) root.findViewById(R.id.weight);
        final EditText ppg = (EditText) root.findViewById(R.id.ppg);
        final EditText color = (EditText) root.findViewById(R.id.color);
        final EditText fermentableID = (EditText) root.findViewById(R.id.fermentableID);
        final Spinner snName = (Spinner) root.findViewById(R.id.fermentableSelector);

        Tools.PopulateDropDown(snName, root.getContext(), m_Fermentables.toArray());
        if (i != null)
            Tools.SetDropDownSelection(snName, m_Fermentables.toArray(), ((FermentableAddition) i).fermentable);

        snName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fermentable h = (Fermentable)parent.getItemAtPosition(position);
                if (h.name != "Select a grain") {

                    ppg.setText(String.valueOf(h.ppg));
                    color.setText(String.valueOf(h.color));
                    fermentableID.setText(h.idString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (i != null) {

            weight.setText(String.valueOf(h.weight));
            ppg.setText(String.valueOf(h.fermentable.ppg));
            color.setText(String.valueOf(h.fermentable.color));
        }*/
    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {

        FermentableAddition f = (FermentableAddition) i;

        TextView tvName = view.view.findViewById(R.id.fermntableName);
        EditText tvWeight = view.view.findViewById(R.id.fermantableWeight);
        TextView tvPPG = view.view.findViewById(R.id.fermantablePPG);
        TextView tvColor = view.view.findViewById(R.id.fermantableColor);


        Tools.SetText(tvName, f.fermentable.name);
        Tools.SetText(tvWeight, f.weight);
        Tools.SetText(tvPPG, "PPG: " + f.fermentable.ppg);
        Tools.SetText(tvColor, "SRM: " + f.fermentable.color);


        tvWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                double dWeight = m_Controller.fermentableChanged(f, s.toString());
                f.weight = dWeight;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //user left the textbox, save the recipe
                    m_Controller.SaveRecipe();
                }
            }
        });
    }

    @Override
    public void onItemClick(View v, ListableObject i) {
        RelativeLayout rlHidden = v.findViewById(R.id.fermantableHiddenExtras);
        //m_Adapter.showAddItemDialog(v.getContext(), i);
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

        Spinner name = (Spinner) d.findViewById(R.id.fermentableSelector);
        EditText weight = (EditText) d.findViewById(R.id.weight);
        EditText ppg = (EditText) d.findViewById(R.id.ppg);
        EditText color = (EditText) d.findViewById(R.id.color);

        Fermentable f = (Fermentable)name.getSelectedItem();

        if (f == null) {
            Tools.ShowToast(d.getContext(), "Error talking to server", Toast.LENGTH_SHORT);
            return false;
        }

        String sName = f.name;
        double dWeight = Tools.ParseDouble(weight.getText().toString());
        double dPPG = Tools.ParseDouble(ppg.getText().toString());
        int iColor = Tools.ParseInt(color.getText().toString());

        if (sName.length() == 0 || dWeight == 0 || dPPG == 0) {
            //InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            FermentableAddition m = (FermentableAddition) i;
            m.fermentable.name = sName;
            m.weight = dWeight;
            m.fermentable.ppg = dPPG;
            m.fermentable.color = iColor;
            m.fermentableID = f.idString;
            //m.recipeID = sExtraInfo;
        } else {
            FermentableAddition malt = new FermentableAddition(sName, dWeight, dPPG, iColor);
            //malt.fermentableID = f.idString;
            malt.fermentableID = java.util.UUID.randomUUID().toString();
            //malt.recipeID = sExtraInfo;
            m_Adapter.add(malt);
        }
        m_Controller.SaveRecipe();*/
        return true;
    }

    public void PopulateList(ArrayList<Fermentable> f) {
        m_Fermentables = f;
    }

    @Override
    public void setReadOnly(NewAdapter.ViewHolder viewHolder, boolean bReadOnly) {

        EditText tvWeight = viewHolder.view.findViewById(R.id.fermantableWeight);
        tvWeight.setEnabled(!bReadOnly);
    }

}
