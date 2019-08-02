package beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls;

import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.SimpleAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.R;

public class YeastAdapterViewControl extends SimpleAdapterViewControl {
    @Override
    public void SetupDialog(View root, ListableObject i) {
        Yeast y = (Yeast)i;

        TextView tvTitle = root.findViewById(R.id.title);
        EditText etLab = root.findViewById(R.id.lab);
        EditText etName = root.findViewById(R.id.name);
        EditText etAttenuation = root.findViewById(R.id.attenuation);

        if (i == null) {
            Tools.SetText(tvTitle, "Add Yeast");
        } else {
            Tools.SetText(tvTitle, "Edit Yeast");
            Tools.SetText(etLab, y.lab);
            Tools.SetText(etName, y.name);
            Tools.SetText(etAttenuation, y.attenuation);
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
        Yeast y = (Yeast)i;


        TextView tvTitle = d.findViewById(R.id.title);
        EditText etLab = d.findViewById(R.id.lab);
        EditText etName = d.findViewById(R.id.name);
        EditText etAttenuation = d.findViewById(R.id.attenuation);

        if (i == null) {
            y = new Yeast();
        }

        y.name = etName.getText().toString();
        y.lab = etLab.getText().toString();
        y.attenuation = Tools.ParseDouble(etAttenuation);

        y.Save();

        if (i == null) {
            m_Adapter.add(y);
        }

        return true;
    }
}
