package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptabletools.Models.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */

public class HopAddition extends ListableObject {
    @Expose
    public String hopID;
    @Expose
    public double amount;
    @Expose
    public String type;
    @Expose
    public int time;
    @Expose
    public Hop hop;

    public static String[] Types = {"Boil", "Dry Hop", "Whirlpool"};

    public HopAddition(String sName, double dAmount, double dAAU, int iTime) {
        hop = new Hop(sName, dAAU);
        name = sName;
        amount = dAmount;
        time = iTime;
    }

    public String name() {
        return hop.name;
    }

}
