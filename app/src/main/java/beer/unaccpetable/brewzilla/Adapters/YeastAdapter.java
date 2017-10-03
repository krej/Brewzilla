package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Ingredient;
import beer.unaccpetable.brewzilla.Ingredients.Yeast;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

public class YeastAdapter extends Adapter {
    public YeastAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtHeader.setText(m_Dataset.get(position).name);

        if (OnlyEmptyIngredientExists()) return;
        Yeast item = (Yeast) m_Dataset.get(position);
        holder.txtFooter.setText("Lab: " + item.Lab);
        holder.txtThirdLine.setText("Attenuation: " + item.Attenuation);
    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting) {
        EditText name = (EditText) d.findViewById(R.id.name);
        EditText lab = (EditText) d.findViewById(R.id.lab);
        EditText att = (EditText) d.findViewById(R.id.attenuation);


        String sName = name.getText().toString();
        String sLab = lab.getText().toString();
        double dAtt = Tools.ParseDouble(att.getText().toString());

        if (sName.length() == 0 || sLab.length() == 0 || dAtt == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            Yeast y = (Yeast)GetClickedItem();
            y.name = sName;
            y.Lab = sLab;
            y.Attenuation = dAtt;
        } else {
            Yeast yeast = new Yeast(sName, sLab, dAtt);
            add(yeast);
        }
        return true;
    }

    @Override
    protected View SetupDialog(Context c, Ingredient i) {
        View root = super.SetupDialog(c,i);

        Yeast h = (Yeast) i;

        if (i != null) {
            EditText name = (EditText) root.findViewById(R.id.name);
            EditText lab = (EditText) root.findViewById(R.id.lab);
            EditText att = (EditText) root.findViewById(R.id.attenuation);

            name.setText(h.name);
            lab.setText(h.Lab);
            att.setText(String.valueOf(h.Attenuation));
        }
        return root;
    }
}
