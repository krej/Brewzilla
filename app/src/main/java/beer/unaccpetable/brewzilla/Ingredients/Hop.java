package beer.unaccpetable.brewzilla.Ingredients;

import org.json.JSONObject;

/**
 * Created by zak on 11/16/2016.
 */

public class Hop extends Ingredient {
    public double amount;
    public double aau;
    public String id;

    public Hop(String sName, double dAAU) {
        name = sName;
        aau = dAAU;
    }

    public Hop(JSONObject o) {

    }
}
