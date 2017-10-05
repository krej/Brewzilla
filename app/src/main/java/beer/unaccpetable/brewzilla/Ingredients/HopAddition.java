package beer.unaccpetable.brewzilla.Ingredients;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import beer.unaccpetable.brewzilla.Network;
import beer.unaccpetable.brewzilla.Tools.JsonExclusion;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by Megatron on 9/25/2017.
 */

public class HopAddition extends ListableObject {
    public String hopID;
    @Expose
    public double amount;
    @Expose
    public String type;
    @Expose
    public int time;
    public Hop hop;

    public HopAddition(String sName, double dAmount, double dAAU) {
        hop = new Hop(sName, dAAU);
        name = sName;
        amount = dAmount;
    }

    public String name() {
        return hop.name;
    }

}
