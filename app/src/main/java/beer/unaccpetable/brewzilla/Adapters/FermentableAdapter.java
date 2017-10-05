package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import beer.unaccpetable.brewzilla.Ingredients.FermentableAddition;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

public class FermentableAdapter extends Adapter {
    public FermentableAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        FermentableAddition item = (FermentableAddition)m_Dataset.get(position);

        holder.txtHeader.setText(item.fermentable.name);
        holder.txtFooter.setText("Weight: " + item.weight + " lbs");
        holder.txtThirdLine.setText("PPG: " + item.fermentable.ppg);
        holder.txtFourthLine.setText(item.fermentable.color + " SRM");
    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting) {

        EditText name = (EditText) d.findViewById(R.id.name);
        EditText weight = (EditText) d.findViewById(R.id.weight);
        EditText ppg = (EditText) d.findViewById(R.id.ppg);
        EditText color = (EditText) d.findViewById(R.id.color);


        String sName = name.getText().toString();
        double dWeight = Tools.ParseDouble(weight.getText().toString());
        double dPPG = Tools.ParseDouble(ppg.getText().toString());
        int iColor = Tools.ParseInt(color.getText().toString());

        if (sName.length() == 0 || dWeight == 0 || dPPG == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            FermentableAddition m = (FermentableAddition)GetClickedItem();
            m.fermentable.name = sName;
            m.weight = dWeight;
            m.fermentable.ppg = dPPG;
            m.fermentable.color = iColor;
        } else {
            FermentableAddition malt = new FermentableAddition(sName, dWeight, dPPG, iColor);
            add(malt);
        }
        return true;
    }

    @Override
    protected View SetupDialog(Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);

        FermentableAddition h = (FermentableAddition) i;

        if (i != null) {
            EditText name = (EditText) root.findViewById(R.id.name);
            EditText weight = (EditText) root.findViewById(R.id.weight);
            EditText ppg = (EditText) root.findViewById(R.id.ppg);
            EditText color = (EditText) root.findViewById(R.id.color);

            name.setText(h.name());
            weight.setText(String.valueOf(h.weight));
            ppg.setText(String.valueOf(h.fermentable.ppg));
            color.setText(String.valueOf(h.fermentable.color));
        }
        return root;
    }
}
