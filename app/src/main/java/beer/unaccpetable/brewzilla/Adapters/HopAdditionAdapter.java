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
import java.util.Arrays;

import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.HopAddition;
import beer.unaccpetable.brewzilla.Tools.Network;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

public class HopAdditionAdapter extends Adapter {

    public HopAdditionAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        HopAddition item = (HopAddition) m_Dataset.get(position);

        holder.txtHeader.setText(item.hop.name);
        holder.txtFooter.setText("Time: " + item.time + " min");
        holder.txtThirdLine.setText(item.amount + " oz");
        holder.txtFourthLine.setText(item.hop.aau + " AAU");
    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting, String sExtraInfo) {
        Spinner name = (Spinner) d.findViewById(R.id.hopSelector);
        Spinner snType = (Spinner) d.findViewById(R.id.hopTypeSpinner);
        EditText amount = (EditText) d.findViewById(R.id.amount);
        EditText aau = (EditText) d.findViewById(R.id.aau);
        EditText time = (EditText) d.findViewById(R.id.time);

        Hop h = (Hop)name.getSelectedItem();
        String sName = h.name;
        double dAmount = Tools.ParseDouble(amount.getText().toString());
        double dAAU = Tools.ParseDouble(aau.getText().toString());
        int iTime = Tools.ParseInt(time.getText().toString());
        String sType = snType.getSelectedItem().toString();

        if (sName.equals("Select a hop") || dAmount == 0 || iTime == 0) {
            InfoMissing(d.getContext());
            return false;
        }

        if (bExisting) {
            HopAddition hopAddition =(HopAddition) GetClickedItem();
            hopAddition.hop.name = sName;
            hopAddition.amount = dAmount;
            hopAddition.hop.aau = dAAU;
            hopAddition.time = iTime;
            hopAddition.recipeID = sExtraInfo;
            hopAddition.hopID = h.id;
            hopAddition.type = sType;
        } else {
            HopAddition hop = new HopAddition(sName, dAmount, dAAU, iTime);
            hop.recipeID = sExtraInfo;
            hop.hopID = h.id;
            hop.type = sType;
            add(hop);
        }
        return true;
    }

    @Override
    protected View SetupDialog(final Context c, ListableObject i) {
        View root = super.SetupDialog(c, i);
        final HopAddition h = (HopAddition) i;

        final Spinner snName = (Spinner) root.findViewById(R.id.hopSelector);
        final Spinner snType = (Spinner) root.findViewById(R.id.hopTypeSpinner);

        final EditText hopID = (EditText) root.findViewById(R.id.hopID);
        final EditText amount = (EditText) root.findViewById(R.id.amount);
        final EditText aau = (EditText) root.findViewById(R.id.aau);
        final EditText time = (EditText) root.findViewById(R.id.time);

        Network.WebRequest(Request.Method.GET, Tools.RestAPIURL() + "/hop", null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                Hop[] hops = gson.fromJson(response, Hop[].class);

                ArrayList<Hop> items = new ArrayList<Hop>();
                items.add(new Hop("Select a hop", -1));
                for (Hop h : hops) {
                    items.add(h);
                }
                Hop[] sItems = items.toArray(new Hop[0]);
                ArrayAdapter<Hop> aa = new ArrayAdapter<Hop>(c, android.R.layout.simple_spinner_dropdown_item, sItems);
                snName.setAdapter(aa);
                if (h != null) {
                    int position = 0;
                    for (Hop hp : items) {
                        if (hp.name.equals(h.hop.name) && hp.aau == h.hop.aau) break;
                        position++;
                    }

                    snName.setSelection(position);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Tools.ShowToast(c, "Error loading hop list", Toast.LENGTH_LONG);
            }
        });

        snName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Hop h = (Hop)parent.getItemAtPosition(position);
                if (h.name != "Select a hop") {

                    aau.setText(String.valueOf(h.aau));
                    hopID.setText(h.id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, HopAddition.Types);
        snType.setAdapter(typeAdapter);

        if (i != null) {

            hopID.setText(h.hop.id);
            amount.setText(String.valueOf(h.amount));
            aau.setText(String.valueOf(h.hop.aau));
            time.setText(String.valueOf(h.time));
            snType.setSelection(Arrays.asList(HopAddition.Types).indexOf(h.type));
        }
        return root;
    }

}
