package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import beer.unaccpetable.brewzilla.Ingredients.YeastAddition;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
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
        if (OnlyEmptyIngredientExists()) return;

        YeastAddition item = (YeastAddition) m_Dataset.get(position);

        holder.txtHeader.setText(item.yeast.name);
        holder.txtFooter.setText("Lab: " + item.yeast.lab);
        holder.txtThirdLine.setText("Attenuation: " + item.yeast.attenuation);
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
            YeastAddition y = (YeastAddition)GetClickedItem();
            y.name = sName;
            y.yeast.lab = sLab;
            y.yeast.attenuation = dAtt;
        } else {
            YeastAddition yeast = new YeastAddition(sName, sLab, dAtt);
            add(yeast);
        }
        return true;
    }

    @Override
    protected View SetupDialog(Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);

        YeastAddition h = (YeastAddition) i;

        if (i != null) {
            EditText name = (EditText) root.findViewById(R.id.name);
            EditText lab = (EditText) root.findViewById(R.id.lab);
            EditText att = (EditText) root.findViewById(R.id.attenuation);

            name.setText(h.name());
            lab.setText(h.yeast.lab);
            att.setText(String.valueOf(h.yeast.attenuation));
        }
        return root;
    }
}
