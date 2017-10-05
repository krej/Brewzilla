package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import beer.unaccpetable.brewzilla.Ingredients.HopAddition;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

public class HopAdapter extends Adapter {

    public HopAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        HopAddition item = (HopAddition) m_Dataset.get(position);

        holder.txtHeader.setText(item.hop.name);
        holder.txtFooter.setText("Time: " + item.time + " min");
        holder.txtThirdLine.setText(item.amount + " oz");
        holder.txtFourthLine.setText(item.hop.aau + " AAU");
    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting) {
        EditText name = (EditText) d.findViewById(R.id.name);
        EditText amount = (EditText) d.findViewById(R.id.amount);
        EditText aau = (EditText) d.findViewById(R.id.aau);
        EditText time = (EditText) d.findViewById(R.id.time);

        String sName = name.getText().toString();
        double dAmount = Tools.ParseDouble(amount.getText().toString());
        double dAAU = Tools.ParseDouble(aau.getText().toString());
        int iTime = Tools.ParseInt(time.getText().toString());

        if (sName.length() == 0 || dAmount == 0 || dAAU == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            HopAddition h =(HopAddition) GetClickedItem();
            h.hop.name = sName;
            h.amount = dAmount;
            h.hop.aau = dAAU;
        } else {
            HopAddition hop = new HopAddition(sName, dAmount, dAAU);
            add(hop);
        }
        return true;
    }

    @Override
    protected View SetupDialog(Context c, ListableObject i) {
        View root = super.SetupDialog(c, i);

        HopAddition h = (HopAddition) i;

        if (i != null) {
            EditText name = (EditText) root.findViewById(R.id.name);
            EditText amount = (EditText) root.findViewById(R.id.amount);
            EditText aau = (EditText) root.findViewById(R.id.aau);
            EditText time = (EditText) root.findViewById(R.id.time);

            name.setText(h.hop.name);
            amount.setText(String.valueOf(h.amount));
            aau.setText(String.valueOf(h.hop.aau));
            time.setText(String.valueOf(h.time));
        }
        return root;
    }
}
