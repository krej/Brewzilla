package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import beer.unaccpetable.brewzilla.Ingredients.Ingredient;
import beer.unaccpetable.brewzilla.Ingredients.Malt;
import beer.unaccpetable.brewzilla.Ingredients.Yeast;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

public class MaltAdapter extends Adapter {
    public MaltAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtHeader.setText(m_Dataset.get(position).Name);

        if (OnlyEmptyIngredientExists()) return;
        Malt item = (Malt)m_Dataset.get(position);
        holder.txtFooter.setText("Weight: " + item.Weight + " lbs");
        holder.txtThirdLine.setText("PPG: " + item.PPG);
        holder.txtFourthLine.setText(item.Color + " SRM");
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
            Malt m = (Malt)GetClickedItem();
            m.Name = sName;
            m.Weight = dWeight;
            m.PPG = dPPG;
            m.Color = iColor;
        } else {
            Malt malt = new Malt(sName, dWeight, dPPG, iColor);
            add(malt);
        }
        return true;
    }

    @Override
    protected View SetupDialog(Context c, Ingredient i) {
        View root = super.SetupDialog(c,i);

        Malt h = (Malt) i;

        if (i != null) {
            EditText name = (EditText) root.findViewById(R.id.name);
            EditText weight = (EditText) root.findViewById(R.id.weight);
            EditText ppg = (EditText) root.findViewById(R.id.ppg);
            EditText color = (EditText) root.findViewById(R.id.color);

            name.setText(h.Name);
            weight.setText(String.valueOf(h.Weight));
            ppg.setText(String.valueOf(h.PPG));
            color.setText(String.valueOf(h.Color));
        }
        return root;
    }
}
