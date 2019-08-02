package beer.unaccpetable.brewzilla.Screens.IngredientManager.AdapterViewControls;

import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.R;

public class FermentableAdapterViewControl extends BaseAdapterViewControl {
    @Override
    public void SetupDialog(View root, ListableObject i) {
        Fermentable f = (Fermentable)i;

        TextView tvTitle = root.findViewById(R.id.title);
        EditText etName = root.findViewById(R.id.name);
        EditText etYield = root.findViewById(R.id.yield);
        EditText etColor = root.findViewById(R.id.color);

        if (f == null) {
            Tools.SetText(tvTitle, "Add Fermentable");
        } else {
            Tools.SetText(tvTitle, "Edit Fermentable");
            Tools.SetText(etName, f.name);
            Tools.SetText(etYield, f.yield);
            Tools.SetText(etColor, f.color);
        }
    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        TextView tv = view.txtHeader;
        Tools.SetText(tv, i.name);
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
        Fermentable f = (Fermentable)i;

        EditText etName = d.findViewById(R.id.name);
        EditText etYield = d.findViewById(R.id.yield);
        EditText etColor = d.findViewById(R.id.color);

        if (f == null) {
            f = new Fermentable();
        }

        f.name = etName.getText().toString();
        f.yield = Tools.ParseDouble(etYield);
        f.color = Tools.ParseDouble(etColor);

        f.Save();

        if (i == null) {
            m_Adapter.add(f);
        }

        return true;
    }
}
