package beer.unaccpetable.brewzilla.Ingredients;

/**
 * Created by zak on 11/16/2016.
 */

public class Malt extends Ingredient {
    public double Weight;
    public double PPG;
    public int Color;

    public Malt(String sName, double dWeight, double dPPG, int iColor) {
        Name = sName;
        Weight = dWeight;
        PPG = dPPG;
        Color = iColor;
    }
}
