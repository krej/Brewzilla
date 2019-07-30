package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */

public class HopAddition extends IngredientAddition {
    @Expose
    public double amount;
    @Expose
    public String type;
    @Expose
    public int time;
    @Expose
    public Hop hop;

    public static String[] Types = {"Boil", "Dry Hop", "Whirlpool"};

    public HopAddition(Hop h) {
        super();
        hop = h;
        name = h.name;
        amount = 0;
        type = Types[0];
        time = 0;
    }

    public HopAddition(String sName, double dAmount, double dAAU, int iTime) {
        super();
        hop = new Hop(sName, dAAU);
        name = sName;
        amount = dAmount;
        time = iTime;
    }

    public String name() {
        return hop.name;
    }

}
