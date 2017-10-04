package beer.unaccpetable.brewzilla.Ingredients;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */

public class FermentableAddition extends ListableObject {
    public String fermentableID;
    public String use;
    public double weight;
    public String id;
    public Fermentable fermentable;

    public FermentableAddition(String sName, double dWeight, double dPPG, int iColor) {
        fermentable = new Fermentable(sName, dPPG, iColor);
        name = sName;
        weight = dWeight;
    }
}
