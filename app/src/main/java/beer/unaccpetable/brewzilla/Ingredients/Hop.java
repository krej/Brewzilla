package beer.unaccpetable.brewzilla.Ingredients;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by zak on 11/16/2016.
 */

public class Hop extends ListableObject {
    @Expose
    public double aau;

    public Hop(String sName, double dAAU) {
        name = sName;
        aau = dAAU;
    }

    public String toString() {
        if (aau >= 0)
            return name + " (AAU: " + aau + ")";
        return name;
    }
}
