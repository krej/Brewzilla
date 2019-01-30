package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

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
