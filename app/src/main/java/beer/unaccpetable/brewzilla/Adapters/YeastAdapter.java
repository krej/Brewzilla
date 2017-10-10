package beer.unaccpetable.brewzilla.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Yeast;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Tools;

import static android.text.InputType.TYPE_CLASS_TEXT;

/**
 * Created by Megatron on 10/9/2017.
 */

public class YeastAdapter extends Adapter {

    public YeastAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        Yeast item = (Yeast) m_Dataset.get(position);

        holder.txtHeader.setText(item.name);
        //holder.txtFooter.setText("AAU: " + item.aau);
    }

    @Override
    public void AddItem(final Context c, ListableObject i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //builder.setView(m_iDialogLayout);
        final boolean bExisting = i != null;

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });


        builder.setView(SetupDialog(c, i));
        final AlertDialog dialog = builder.create();


        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (AddItem(dialog, bExisting, ""))
                    dialog.dismiss();
            }
        });
    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting, String sExtraData) {
        final Spinner snName = (Spinner) d.findViewById(R.id.yeastSelector);
        final EditText lab = (EditText) d.findViewById(R.id.lab);
        final EditText att = (EditText) d.findViewById(R.id.attenuation);
        final EditText yeastID = (EditText) d.findViewById(R.id.yeastID);
        final EditText name = (EditText) d.findViewById(R.id.name);

        String sName = name.getText().toString();
        String sLab = lab.getText().toString();
        double attenuation = Tools.ParseDouble(att.getText().toString());
        String sID = yeastID.getText().toString();

        if (sName.length() == 0 || sLab.length() == 0 || attenuation == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        Yeast yeast;

        if (bExisting) {
            yeast =(Yeast) GetClickedItem();
            yeast.name = sName;
            yeast.lab = sLab;
            yeast.attenuation = attenuation;
            yeast.id = sID;
        } else {
            yeast = new Yeast(sName, sLab, attenuation);
            yeast.id = sID;
            add(yeast);
        }

        yeast.Save();

        return true;
    }

    @Override
    protected View SetupDialog(final Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);


        final Spinner snName = (Spinner) root.findViewById(R.id.yeastSelector);
        final EditText lab = (EditText) root.findViewById(R.id.lab);
        final EditText att = (EditText) root.findViewById(R.id.attenuation);
        final EditText yeastID = (EditText) root.findViewById(R.id.yeastID);
        final EditText name = (EditText) root.findViewById(R.id.name);

        //Rearrange the HopAddition screen for Hops. I'm not sure if I want to keep it like this but its easy for now
        snName.setVisibility(View.GONE);
        lab.setEnabled(true);
        name.setEnabled(true);
        name.setVisibility(View.VISIBLE);
        att.setEnabled(true);

        Yeast h = (Yeast)i;

        if (h != null ) {
            yeastID.setText(h.id);
            lab.setText(h.lab);
            name.setText(h.name);
            att.setText(String.valueOf(h.attenuation));
        }

        return root;
    }
}
