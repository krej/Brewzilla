package beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls;

import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.SimpleAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Adjunct;
import beer.unaccpetable.brewzilla.R;

public class AdjunctAdapterViewControl extends SimpleAdapterViewControl {
    @Override
    public void SetupDialog(View root, ListableObject i) {
        Adjunct a = (Adjunct)i;

        TextView tvTitle = root.findViewById(R.id.title);
        EditText etName = root.findViewById(R.id.name);

        if (i == null) {
            Tools.SetText(tvTitle, "Add Adjunct");
        } else {
            Tools.SetText(tvTitle, "Edit Adjunct");
            Tools.SetText(etName, a.name);
        }
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
        Adjunct a = (Adjunct)i;

        EditText etName = d.findViewById(R.id.name);

        if (i == null)
            a = new Adjunct();

        a.name = etName.getText().toString();

        a.Save();

        if (i == null) {
            m_Adapter.add(a);
        }

        return true;
    }
}
