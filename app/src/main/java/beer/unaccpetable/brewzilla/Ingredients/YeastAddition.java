package beer.unaccpetable.brewzilla.Ingredients;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */

public class YeastAddition extends ListableObject {
    public String yeastID;
    public String id;
    public Yeast yeast;

    public YeastAddition(String sProduct, String sLab,double dAttenuation) {
        name = sProduct;
        yeast = new Yeast(sProduct, sLab, dAttenuation);
    }
}
