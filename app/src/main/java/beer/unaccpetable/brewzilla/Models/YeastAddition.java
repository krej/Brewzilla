package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */


public class YeastAddition extends IngredientAddition {
    @Expose
    public Yeast yeast;

    public YeastAddition(Yeast y) {
        super();
        yeast = y;
    }

    public YeastAddition(String sProduct, String sLab,double dAttenuation) {
        super();
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
