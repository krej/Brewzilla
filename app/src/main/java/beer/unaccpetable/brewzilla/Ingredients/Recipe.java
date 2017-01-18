package beer.unaccpetable.brewzilla.Ingredients;


import java.util.List;

/**
 * Created by zak on 1/4/2017.
 */

public class Recipe extends Ingredient  {
    public String id;

    public void Recipe(String sName) {
        Name = sName;
    }

    public void Recipe(String sName, String sID) {
        Recipe(sName);
        id = sID;
    }
}
