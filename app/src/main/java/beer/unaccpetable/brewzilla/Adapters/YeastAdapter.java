package beer.unaccpetable.brewzilla.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unacceptable.unacceptablelibrary.Adapters.Adapter;
import com.unacceptable.unacceptablelibrary.Tools.Network;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.R;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Preferences;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

/**
 * Created by Megatron on 10/9/2017.
 */

public class YeastAdapter extends Adapter {

    public boolean IngredientManagerMode = false;

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
            yeast.idString = sID;
        } else {
            yeast = new Yeast(sName, sLab, attenuation);
            yeast.idString = sID;
            add(yeast);
        }

        yeast.Save();

        return true;
    }

    @Override
    protected View SetupDialog(final Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);

        final Yeast h = (Yeast)i;

        final Spinner snName = (Spinner) root.findViewById(R.id.yeastSelector);
        final EditText lab = (EditText) root.findViewById(R.id.lab);
        final EditText att = (EditText) root.findViewById(R.id.attenuation);
        final EditText yeastID = (EditText) root.findViewById(R.id.yeastID);
        final EditText name = (EditText) root.findViewById(R.id.name);

        Network.WebRequest(Request.Method.GET, Preferences.BeerNetAPIURL() + "/yeast", null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                Yeast[] yeasts = gson.fromJson(response, Yeast[].class);

                ArrayList<Yeast> items = new ArrayList<Yeast>();
                items.add(new Yeast("Select a yeast", "", -1));
                for (Yeast h : yeasts) {
                    items.add(h);
                }
                Yeast[] sItems = items.toArray(new Yeast[0]);
                ArrayAdapter<Yeast> aa = new ArrayAdapter<Yeast>(c, android.R.layout.simple_spinner_dropdown_item, sItems);
                snName.setAdapter(aa);
                if (h != null) {
                    int position = 0;
                    for (Yeast hp : items) {
                        if (hp.name.equals(h.name) && hp.lab.equals(h.lab) && hp.attenuation == h.attenuation) break;
                        position++;
                    }

                    snName.setSelection(position);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Tools.ShowToast(c, "Error loading yeast list", Toast.LENGTH_LONG);
            }
        });

        snName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Yeast h = (Yeast)parent.getItemAtPosition(position);
                if (h.name != "Select a grain") {

                    lab.setText(String.valueOf(h.lab));
                    att.setText(String.valueOf(h.attenuation));
                    yeastID.setText(h.idString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Rearrange the HopAddition screen for Hops. I'm not sure if I want to keep it like this but its easy for now
        if (IngredientManagerMode)
            snName.setVisibility(View.GONE);
        lab.setEnabled(true);
        name.setEnabled(true);
        name.setVisibility(View.VISIBLE);
        att.setEnabled(true);

        Yeast h1 = (Yeast)i;

        if (h1 != null ) {
            yeastID.setText(h1.idString);
            lab.setText(h1.lab);
            name.setText(h1.name);
            att.setText(String.valueOf(h.attenuation));
        }

        return root;
    }
}
