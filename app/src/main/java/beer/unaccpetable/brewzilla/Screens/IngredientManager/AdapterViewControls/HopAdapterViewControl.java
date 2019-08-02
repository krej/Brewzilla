package beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls;

import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Adapters.SimpleAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.R;

import static android.text.InputType.TYPE_CLASS_TEXT;

public class HopAdapterViewControl extends SimpleAdapterViewControl {
    @Override
    public void SetupDialog(View root, ListableObject i) {
        Hop h = (Hop)i;

        TextView tvTitle = root.findViewById(R.id.title);
        EditText etAAU = root.findViewById(R.id.aau);
        EditText etName = root.findViewById(R.id.name);

        if (i == null) {
            Tools.SetText(tvTitle, "Add Hop");
        } else {
            Tools.SetText(tvTitle, "Edit Hop");

            Tools.SetText(etName, h.name);
            Tools.SetText(etAAU, h.aau);
        }


        /*final Spinner snName = (Spinner) root.findViewById(R.id.hopSelector);
        final Spinner snType = (Spinner) root.findViewById(R.id.hopTypeSpinner);
        final TextView hopType = root.findViewById(R.id.hopType);

        final EditText hopID = (EditText) root.findViewById(R.id.hopID);
        final EditText amount = (EditText) root.findViewById(R.id.amount);
        final EditText aau = (EditText) root.findViewById(R.id.aau);
        final EditText time = (EditText) root.findViewById(R.id.time);
        final TextView title = root.findViewById(R.id.title);

        //Rearrange the HopAddition screen for Hops. I'm not sure if I want to keep it like this but its easy for now
        snName.setVisibility(View.GONE);
        amount.setInputType(TYPE_CLASS_TEXT); //used for Name
        amount.setHint("Name");
        aau.setEnabled(true);
        time.setVisibility(View.GONE);
        snType.setVisibility(View.GONE);
        hopType.setVisibility(View.GONE);

        Hop h = (Hop)i;

        if (h != null ) {
            hopID.setText(h.idString);
            amount.setText(h.name);
            aau.setText(String.valueOf(h.aau));
            title.setText("Edit Hop"); //temp until i delete this adapter class
        }*/

    }

    @Override
    public void onItemClick(View v, ListableObject i) {
        m_Adapter.showAddItemDialog(v.getContext(), i);
    }

    @Override
    public void onItemLongPress(View v, ListableObject i) {

    }

    @Override
    public boolean onDialogOkClicked(Dialog d, ListableObject i) {
        Hop h = (Hop)i;

        EditText etAAU = d.findViewById(R.id.aau);
        EditText etName = d.findViewById(R.id.name);

        String sName = etName.getText().toString();
        double dAAU = Tools.ParseDouble(etAAU);

        if (i == null) {
            h = new Hop(sName, dAAU);
        } else {
            h.name = sName;
            h.aau = dAAU;
        }

        h.Save();

        if (i == null) {
            m_Adapter.add(h);
        }

        return true;
    }
}
