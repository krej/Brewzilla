package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Context;
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
import com.unacceptable.unacceptabletools.Adapters.Adapter;
import com.unacceptable.unacceptabletools.Tools.Network;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Models.YeastAddition;
import beer.unaccpetable.brewzilla.R;
import com.unacceptable.unacceptabletools.Models.ListableObject;
import com.unacceptable.unacceptabletools.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

public class YeastAdditionAdapter extends Adapter {
    public YeastAdditionAdapter(int iLayout, int iDialogLayout) {
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
    protected boolean AddItem(Dialog d, boolean bExisting, String sExtraInfo) {
        final Spinner snName = (Spinner) d.findViewById(R.id.yeastSelector);
        EditText lab = (EditText) d.findViewById(R.id.lab);
        EditText att = (EditText) d.findViewById(R.id.attenuation);
        EditText txtYeastID = (EditText) d.findViewById(R.id.yeastID);

        Yeast f = (Yeast) snName.getSelectedItem();

        String sName = f.name;
        String sLab = lab.getText().toString();
        double dAtt = Tools.ParseDouble(att.getText().toString());
        String yeastID = txtYeastID.getText().toString();

        if (sName.length() == 0 || sLab.length() == 0 || dAtt == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            YeastAddition y = (YeastAddition)GetClickedItem();
            y.name = sName;
            y.yeast.lab = sLab;
            y.yeast.attenuation = dAtt;
            y.yeastID = yeastID;
            y.recipeID = sExtraInfo;
        } else {
            YeastAddition yeast = new YeastAddition(sName, sLab, dAtt);
            yeast.yeastID = yeastID;
            yeast.recipeID = sExtraInfo;
            add(yeast);
        }
        return true;
    }

    @Override
    protected View SetupDialog(final Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);

        final YeastAddition h = (YeastAddition) i;

        final Spinner snName = (Spinner) root.findViewById(R.id.yeastSelector);
        final EditText lab = (EditText) root.findViewById(R.id.lab);
        final EditText att = (EditText) root.findViewById(R.id.attenuation);
        final EditText yeastID = (EditText) root.findViewById(R.id.yeastID);

        Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/yeast", null, new Response.Listener<String>() {
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
                        if (hp.name.equals(h.yeast.name) && hp.lab.equals(h.yeast.lab) && hp.attenuation == h.yeast.attenuation) break;
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

        if (i != null) {

            lab.setText(h.yeast.lab);
            att.setText(String.valueOf(h.yeast.attenuation));
        }
        return root;
    }
}
