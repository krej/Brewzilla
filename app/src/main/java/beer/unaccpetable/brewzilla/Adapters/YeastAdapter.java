package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.widget.EditText;

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
        holder.txtHeader.setText(m_Dataset.get(position).Name);

        if (OnlyEmptyIngredientExists()) return;
        Yeast item = (Yeast) m_Dataset.get(position);
        holder.txtFooter.setText("Lab: " + item.Lab);
        holder.txtThirdLine.setText("Attenuation: " + item.Attenuation);
    }

    @Override
    protected boolean AddItem(Dialog d) {
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

        Yeast yeast = new Yeast(sName, sLab, dAtt);
        add(yeast);

        return true;
    }
}
