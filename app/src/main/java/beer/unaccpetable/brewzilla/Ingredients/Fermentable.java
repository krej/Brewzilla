package beer.unaccpetable.brewzilla.Ingredients;

/**
 * Created by zak on 11/16/2016.
 */

public class Fermentable extends Ingredient {
    public double Weight;
    public double PPG;
    public int Color;

    public Fermentable(String sName, double dWeight, double dPPG, int iColor) {
        name = sName;
        Weight = dWeight;
        PPG = dPPG;
        Color = iColor;
    }
}
