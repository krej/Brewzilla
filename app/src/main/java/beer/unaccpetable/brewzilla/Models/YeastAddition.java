package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */

//TODO: Delete!!!


public class YeastAddition extends ListableObject {
    @Expose
    public String yeastID;
    public Yeast yeast;
    @Expose
    public String recipeID;

    public YeastAddition(String sProduct, String sLab,double dAttenuation) {
        name = sProduct;
        yeast = new Yeast(sProduct, sLab, dAttenuation);
    }

    public String name() {
        return yeast.name;
    }

    public String toString() {
        return name;
    }
}
