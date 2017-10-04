package beer.unaccpetable.brewzilla.Ingredients;

/**
 * Created by zak on 11/16/2016.
 */

public class Fermentable extends Ingredient {
    public double ppg;
    public int color;

    public Fermentable(String sName, double dPPG, int iColor) {
        name = sName;
        ppg = dPPG;
        color = iColor;
    }
}
