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
import com.unacceptable.unacceptablelibrary.Adapters.Adapter;
import com.unacceptable.unacceptablelibrary.Tools.Network;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.R;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

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

        holder.txtHeader.setText(item.fermentable.name);
        holder.txtFooter.setText("Weight: " + item.weight + " lbs");
        holder.txtThirdLine.setText("PPG: " + item.fermentable.ppg);
        holder.txtFourthLine.setText(item.fermentable.color + " SRM");
    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting, String sExtraInfo) {

        Spinner name = (Spinner) d.findViewById(R.id.fermentableSelector);
        EditText weight = (EditText) d.findViewById(R.id.weight);
        EditText ppg = (EditText) d.findViewById(R.id.ppg);
        EditText color = (EditText) d.findViewById(R.id.color);

        Fermentable f = (Fermentable)name.getSelectedItem();

        if (f == null) {
            Tools.ShowToast(d.getContext(), "Error talking to server", Toast.LENGTH_SHORT);
            return false;
        }

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
            m.fermentable.name = sName;
            m.weight = dWeight;
            m.fermentable.ppg = dPPG;
            m.fermentable.color = iColor;
            m.fermentableID = f.idString;
            //m.recipeID = sExtraInfo;
        } else {
            FermentableAddition malt = new FermentableAddition(sName, dWeight, dPPG, iColor);
            malt.fermentableID = f.idString;
            //malt.recipeID = sExtraInfo;
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

        Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/fermentable", null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                Fermentable[] hops = gson.fromJson(response, Fermentable[].class);

                ArrayList<Fermentable> items = new ArrayList<Fermentable>();
                items.add(new Fermentable("Select a grain", -1, -1));
                for (Fermentable h : hops) {
                    items.add(h);
                }
                Fermentable[] sItems = items.toArray(new Fermentable[0]);
                ArrayAdapter<Fermentable> aa = new ArrayAdapter<Fermentable>(c, android.R.layout.simple_spinner_dropdown_item, sItems);
                snName.setAdapter(aa);
                if (h != null) {
                    int position = 0;
                    for (Fermentable hp : items) {
                        if (hp.name.equals(h.fermentable.name) && hp.color == h.fermentable.color && hp.ppg == h.fermentable.ppg) break;
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
                Fermentable h = (Fermentable)parent.getItemAtPosition(position);
                if (h.name != "Select a grain") {

                    ppg.setText(String.valueOf(h.ppg));
                    color.setText(String.valueOf(h.color));
                    fermentableID.setText(h.idString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (i != null) {

            weight.setText(String.valueOf(h.weight));
            ppg.setText(String.valueOf(h.fermentable.ppg));
            color.setText(String.valueOf(h.fermentable.color));
        }
        return root;
    }
}
