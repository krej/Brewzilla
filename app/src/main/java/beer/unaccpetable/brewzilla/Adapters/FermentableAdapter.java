package beer.unaccpetable.brewzilla.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.R;

import com.unacceptable.unacceptablelibrary.Adapters.Adapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.view.View.GONE;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

/**
 * Created by Megatron on 10/9/2017.
 */

public class FermentableAdapter extends Adapter {

    public FermentableAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        Fermentable item = (Fermentable) m_Dataset.get(position);

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
        Spinner name = (Spinner) d.findViewById(R.id.fermentableSelector);
        EditText txtName = (EditText) d.findViewById(R.id.weight); //reuse weight for name in the editor
        EditText ppg = (EditText) d.findViewById(R.id.ppg);
        EditText color = (EditText) d.findViewById(R.id.color);
        EditText fermentableID = (EditText) d.findViewById(R.id.fermentableID);

        String sName = txtName.getText().toString();
        double dPPG = Tools.ParseDouble(ppg.getText().toString());
        int iColor = Tools.ParseInt(color.getText().toString());
        String sID = fermentableID.getText().toString();

        if (sName.length() == 0 || dPPG == 0 || iColor == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        Fermentable fermentables;

        if (bExisting) {
            fermentables =(Fermentable) GetClickedItem();
            fermentables.name = sName;
            fermentables.ppg = dPPG;
            fermentables.color = iColor;
            fermentables.idString = sID;
            fermentables.type = "Dry Extract"; //hard code for now...
        } else {
            fermentables = new Fermentable(sName, dPPG, iColor);
            fermentables.idString = sID;
            fermentables.type = "Dry Extract";
            add(fermentables);
        }

        fermentables.Save();

        return true;
    }

    @Override
    protected View SetupDialog(final Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);

        Spinner name = (Spinner) root.findViewById(R.id.fermentableSelector);
        EditText txtName = (EditText) root.findViewById(R.id.weight); //reuse weight for name in the editor
        EditText ppg = (EditText) root.findViewById(R.id.ppg);
        EditText color = (EditText) root.findViewById(R.id.color);
        EditText fermentableID = (EditText) root.findViewById(R.id.fermentableID);
        TextView title = root.findViewById(R.id.title);

        name.setVisibility(GONE);
        txtName.setInputType(TYPE_CLASS_TEXT);
        ppg.setEnabled(true);
        color.setEnabled(true);

        Fermentable h = (Fermentable) i;

        if (h != null ) {
            fermentableID.setText(h.idString);
            txtName.setText(h.name);
            ppg.setText(String.valueOf(h.ppg));
            color.setText(String.valueOf(h.color));
            title.setText("Edit Malt");
        }

        return root;
    }

}
