package beer.unaccpetable.brewzilla.Ingredients;

import org.json.JSONObject;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by zak on 11/16/2016.
 */

public class Hop extends ListableObject {
    public double amount;
    public double aau;

    public Hop(String sName, double dAAU) {
        name = sName;
        aau = dAAU;
    }

    public Hop(JSONObject o) {

    }

    public String toString() {
        if (aau >= 0)
            return name + " (AAU: " + aau + ")";
        return name;
    }
}
