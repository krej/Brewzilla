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

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Ingredients.Fermentables;
import beer.unaccpetable.brewzilla.Ingredients.FermentableAddition;
import beer.unaccpetable.brewzilla.Tools.Network;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

public class FermentableAdditionAdapter extends Adapter {

    public FermentableAdditionAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        FermentableAddition item = (FermentableAddition)m_Dataset.get(position);

        holder.txtHeader.setText(item.fermentables.name);
        holder.txtFooter.setText("Weight: " + item.weight + " lbs");
        holder.txtThirdLine.setText("PPG: " + item.fermentables.ppg);
        holder.txtFourthLine.setText(item.fermentables.color + " SRM");
    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting, String sExtraInfo) {

        Spinner name = (Spinner) d.findViewById(R.id.fermentableSelector);
        EditText weight = (EditText) d.findViewById(R.id.weight);
        EditText ppg = (EditText) d.findViewById(R.id.ppg);
        EditText color = (EditText) d.findViewById(R.id.color);

        Fermentables f = (Fermentables)name.getSelectedItem();
        String sName = f.name;
        double dWeight = Tools.ParseDouble(weight.getText().toString());
        double dPPG = Tools.ParseDouble(ppg.getText().toString());
        int iColor = Tools.ParseInt(color.getText().toString());

        if (sName.length() == 0 || dWeight == 0 || dPPG == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            FermentableAddition m = (FermentableAddition)GetClickedItem();
            m.fermentables.name = sName;
            m.weight = dWeight;
            m.fermentables.ppg = dPPG;
            m.fermentables.color = iColor;
            m.fermentableID = f.id;
            m.recipeID = sExtraInfo;
        } else {
            FermentableAddition malt = new FermentableAddition(sName, dWeight, dPPG, iColor);
            malt.fermentableID = f.id;
            malt.recipeID = sExtraInfo;
            add(malt);
        }
        return true;
    }

    @Override
    protected View SetupDialog(final Context c, ListableObject i) {
        View root = super.SetupDialog(c,i);

        final FermentableAddition h = (FermentableAddition) i;
        final EditText weight = (EditText) root.findViewById(R.id.weight);
        final EditText ppg = (EditText) root.findViewById(R.id.ppg);
        final EditText color = (EditText) root.findViewById(R.id.color);
        final EditText fermentableID = (EditText) root.findViewById(R.id.fermentableID);
        final Spinner snName = (Spinner) root.findViewById(R.id.fermentableSelector);

        Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/fermentables", null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                Fermentables[] hops = gson.fromJson(response, Fermentables[].class);

                ArrayList<Fermentables> items = new ArrayList<Fermentables>();
                items.add(new Fermentables("Select a grain", -1, -1));
                for (Fermentables h : hops) {
                    items.add(h);
                }
                Fermentables[] sItems = items.toArray(new Fermentables[0]);
                ArrayAdapter<Fermentables> aa = new ArrayAdapter<Fermentables>(c, android.R.layout.simple_spinner_dropdown_item, sItems);
                snName.setAdapter(aa);
                if (h != null) {
                    int position = 0;
                    for (Fermentables hp : items) {
                        if (hp.name.equals(h.fermentables.name) && hp.color == h.fermentables.color && hp.ppg == h.fermentables.ppg) break;
                        position++;
                    }

                    snName.setSelection(position);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Tools.ShowToast(c, "Error loading grain list", Toast.LENGTH_LONG);
            }
        });

        snName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fermentables h = (Fermentables)parent.getItemAtPosition(position);
                if (h.name != "Select a grain") {

                    ppg.setText(String.valueOf(h.ppg));
                    color.setText(String.valueOf(h.color));
                    fermentableID.setText(h.id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (i != null) {

            weight.setText(String.valueOf(h.weight));
            ppg.setText(String.valueOf(h.fermentables.ppg));
            color.setText(String.valueOf(h.fermentables.color));
        }
        return root;
    }
}