package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.AdjunctAddition;
import beer.unaccpetable.brewzilla.R;

public class AdjunctAdditionAdapterViewControl extends BaseAdapterViewControl {
    private RecipeEditorController m_Controller;

    public AdjunctAdditionAdapterViewControl(RecipeEditorController controller) {
        m_Controller = controller;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {

    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        AdjunctAddition aa = (AdjunctAddition)i;

        TextView tv = view.view.findViewById(R.id.name);
        EditText etAmount = view.view.findViewById(R.id.adjunctAmount);
        EditText etAmountUnit = view.view.findViewById(R.id.adjunctAmountUnit);
        EditText etTime = view.view.findViewById(R.id.adjunctTime);
        EditText etTimeUnit = view.view.findViewById(R.id.adjunctTimeUnit);

        Tools.SetText(tv, aa.adjunct.name);
        Tools.SetText(etAmount, aa.amount);
        Tools.SetText(etAmountUnit, aa.unit);
        Tools.SetText(etTime, aa.time);
        Tools.SetText(etTimeUnit, aa.timeUnit);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sAmount = etAmount.getText().toString();
                String sAmountUnit = etAmountUnit.getText().toString();
                String sTime = etTime.getText().toString();
                String sTimeUnit = etTimeUnit.getText().toString();

                AdjunctAddition a  = m_Controller.adjunctChanged(aa, sAmount, sAmountUnit, sTime, sTimeUnit);
                aa.amount = a.amount;
                aa.unit = a.unit;
                aa.time = a.time;
                aa.timeUnit = a.timeUnit;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etAmount.addTextChangedListener(tw);
        etAmountUnit.addTextChangedListener(tw);
        etTime.addTextChangedListener(tw);
        etTimeUnit.addTextChangedListener(tw);

        etAmount.setOnFocusChangeListener(m_Controller.createFocusChangeListener());
        etAmountUnit.setOnFocusChangeListener(m_Controller.createFocusChangeListener());
        etTime.setOnFocusChangeListener(m_Controller.createFocusChangeListener());
        etTimeUnit.setOnFocusChangeListener(m_Controller.createFocusChangeListener());
    }

    @Override
    public void onItemClick(View v, ListableObject i) {

    }

    @Override
    public void onItemLongPress(View v, ListableObject i) {

    }

    @Override
    public boolean onDialogOkClicked(Dialog d, ListableObject i) {
        return false;
    }
}
