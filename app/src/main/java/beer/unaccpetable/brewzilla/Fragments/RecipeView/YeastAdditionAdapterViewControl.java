package beer.unaccpetable.brewzilla.Fragments.RecipeView;

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
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditorController;

public class YeastAdditionAdapterViewControl extends BaseAdapterViewControl {

    public YeastAdditionAdapterViewControl() {

    }

    @Override
    public void SetupDialog(View root, ListableObject i) {

    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        YeastAddition y = (YeastAddition) i;


        TextView tvLab = view.view.findViewById(R.id.yeastLab);
        TextView tvName = view.view.findViewById(R.id.yeastName);
        TextView tvAttenuation = view.view.findViewById(R.id.attenuation);

        Tools.SetText(tvLab, y.yeast.lab);
        Tools.SetText(tvName, y.yeast.name);
        Tools.SetText(tvAttenuation, "Attenuation: " + y.yeast.attenuation);

    }

    @Override
    public void onItemClick(View v, ListableObject i) {
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

        return true;
    }

}
