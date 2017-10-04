package beer.unaccpetable.brewzilla.Ingredients;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */

public class HopAddition extends ListableObject {
    public String hopID;
    public double amount;
    public String type;
    public double time;
    public String id;
    public Hop hop;

    public HopAddition(String sName, double dAmount, double dAAU) {
        hop = new Hop(sName, dAAU);
        name = sName;
        amount = dAmount;
    }
}
