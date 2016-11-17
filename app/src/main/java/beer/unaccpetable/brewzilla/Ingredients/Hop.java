package beer.unaccpetable.brewzilla.Ingredients;

/**
 * Created by zak on 11/16/2016.
 */

public class Hop extends Ingredient {
    public double Amount;
    public double AAU;
    public int Time;

    public Hop(String sName, double dAmount, double dAAU, int iTime) {
        Name = sName;
        Amount = dAmount;
        AAU = dAAU;
        Time = iTime;
    }
}
