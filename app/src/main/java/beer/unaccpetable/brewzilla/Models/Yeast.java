package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by zak on 11/16/2016.
 */

public class Yeast extends ListableObject {
    @Expose
    public String lab;
    @Expose
    public double attenuation;

    public Yeast(String sProduct, String sLab,double dAttenuation) {
        name = sProduct;
        lab = sLab;
        attenuation = dAttenuation;
    }


    public String toString() {
        return lab + " " + name;
    }
}
