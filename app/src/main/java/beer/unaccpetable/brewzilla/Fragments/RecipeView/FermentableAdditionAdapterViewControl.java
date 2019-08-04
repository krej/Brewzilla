package beer.unaccpetable.brewzilla.Fragments.RecipeView;

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
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditorController;

public class FermentableAdditionAdapterViewControl extends BaseAdapterViewControl {
    private RecipeViewController m_Controller;

    public FermentableAdditionAdapterViewControl(RecipeViewController controller) {
        m_Controller = controller;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {

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
        Tools.SetText(tvPPG, "Yield: " + f.fermentable.yield);
        Tools.SetText(tvColor, "SRM: " + f.fermentable.color);


        //tvWeight.setSelectAllOnFocus(true);

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

        tvWeight.setOnFocusChangeListener(m_Controller.createFocusChangeListener());
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
        return true;
    }

    @Override
    public void setReadOnly(NewAdapter.ViewHolder viewHolder, boolean bReadOnly) {

        EditText tvWeight = viewHolder.view.findViewById(R.id.fermantableWeight);
        tvWeight.setEnabled(!bReadOnly);
    }

}
