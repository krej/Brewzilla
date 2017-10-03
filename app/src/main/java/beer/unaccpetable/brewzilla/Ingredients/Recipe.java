package beer.unaccpetable.brewzilla.Ingredients;


import java.util.List;

/**
 * Created by zak on 1/4/2017.
 */

public class Recipe extends Ingredient  {
    public String style;
    public String description;
    public double abv;
    public double ibu;
    public double fg;
    public double og;
    public double srm;
    public double version;
    public String id;
    public String test;
    public List<HopAddition> hops;
    public List<YeastAddition> yeasts;
    public List<FermentableAddition> fermentables;


    public void Recipe(String sName) {
        name = sName;
    }

    public void Recipe(String sName, String sID) {
        Recipe(sName);
        id = sID;
    }
}
