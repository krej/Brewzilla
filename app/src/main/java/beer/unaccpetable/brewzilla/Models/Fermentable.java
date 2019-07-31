package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by zak on 11/16/2016.
 */

public class Fermentable extends ListableObject {
    @Expose
    public double ppg;
    @Expose
    public float color;
    @Expose
    public String type;
    @Expose
    public String maltster;
    @Expose
    public String createdByUserId;
    @Expose
    public float yield;

    public Fermentable(String sName, double dPPG, int iColor) {
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
