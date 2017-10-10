package beer.unaccpetable.brewzilla.Ingredients;

import com.google.gson.annotations.Expose;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by zak on 11/16/2016.
 */

public class Fermentables extends ListableObject {
    @Expose
    public double ppg;
    @Expose
    public int color;
    @Expose
    public String type;

    public Fermentables(String sName, double dPPG, int iColor) {
        name = sName;
        ppg = dPPG;
        color = iColor;
    }

    public String toString() {
        if (ppg < 0 && color < 0)
            return "Select a grain";
        return name;

    }
}
