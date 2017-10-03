package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Ingredient;
import beer.unaccpetable.brewzilla.R;
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
        holder.txtHeader.setText(m_Dataset.get(position).name);

        if (OnlyEmptyIngredientExists()) return;

        Hop item = (Hop)m_Dataset.get(position);
        holder.txtFooter.setText("Time: " + 0 + " min");
        holder.txtThirdLine.setText(item.amount + " oz");
        holder.txtFourthLine.setText(item.aau + " AAU");
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
            Hop h =(Hop) GetClickedItem();
            h.name = sName;
            h.amount = dAmount;
            h.aau = dAAU;
        } else {
            Hop hop = new Hop(sName, dAmount, dAAU);
            add(hop);
        }
        return true;
    }

    @Override
    protected View SetupDialog(Context c, Ingredient i) {
        View root = super.SetupDialog(c,i);

        Hop h = (Hop)i;

        if (i != null) {
            EditText name = (EditText) root.findViewById(R.id.name);
            EditText amount = (EditText) root.findViewById(R.id.amount);
            EditText aau = (EditText) root.findViewById(R.id.aau);
            EditText time = (EditText) root.findViewById(R.id.time);

            name.setText(h.name);
            amount.setText(String.valueOf(h.amount));
            aau.setText(String.valueOf(h.aau));
        }
        return root;
    }
}
