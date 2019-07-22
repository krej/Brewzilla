package beer.unaccpetable.brewzilla.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.R;

import com.unacceptable.unacceptablelibrary.Adapters.Adapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import static android.text.InputType.TYPE_CLASS_TEXT;

/**
 * Created by Megatron on 10/9/2017.
 */

public class HopAdapter extends Adapter {

    public HopAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        Hop item = (Hop) m_Dataset.get(position);

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
        EditText txtAmount = (EditText) d.findViewById(R.id.amount);
        EditText aau = (EditText) d.findViewById(R.id.aau);
        EditText hopID = (EditText) d.findViewById(R.id.hopID);

        String sName = txtAmount.getText().toString();
        double dAAU = Tools.ParseDouble(aau.getText().toString());
        String sID = hopID.getText().toString();

        if (sName.length() == 0 || dAAU == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        Hop hop;

        if (bExisting) {
            hop =(Hop) GetClickedItem();
            hop.name = sName;
            hop.aau = dAAU;
            hop.idString = sID;
        } else {
            hop = new Hop(sName, dAAU);
            hop.idString = sID;
            add(hop);
        }

        hop.Save();

        return true;
    }

    @Override
    protected View SetupDialog(final Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);

        final Spinner snName = (Spinner) root.findViewById(R.id.hopSelector);
        final Spinner snType = (Spinner) root.findViewById(R.id.hopTypeSpinner);
        final TextView hopType = root.findViewById(R.id.hopType);

        final EditText hopID = (EditText) root.findViewById(R.id.hopID);
        final EditText amount = (EditText) root.findViewById(R.id.amount);
        final EditText aau = (EditText) root.findViewById(R.id.aau);
        final EditText time = (EditText) root.findViewById(R.id.time);
        final TextView title = root.findViewById(R.id.title);

        //Rearrange the HopAddition screen for Hops. I'm not sure if I want to keep it like this but its easy for now
        snName.setVisibility(View.GONE);
        amount.setInputType(TYPE_CLASS_TEXT); //used for Name
        amount.setHint("Name");
        aau.setEnabled(true);
        time.setVisibility(View.GONE);
        snType.setVisibility(View.GONE);
        hopType.setVisibility(View.GONE);

        Hop h = (Hop)i;

        if (h != null ) {
            hopID.setText(h.idString);
            amount.setText(h.name);
            aau.setText(String.valueOf(h.aau));
            title.setText("Edit Hop"); //temp until i delete this adapter class
        }

        return root;
    }
}
