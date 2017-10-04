package beer.unaccpetable.brewzilla.Ingredients;

/**
 * Created by zak on 11/16/2016.
 */

public class Yeast extends Ingredient {
    public String lab;
    public double attenuation;

    public Yeast(String sProduct, String sLab,double dAttenuation) {
        name = sProduct;
        lab = sLab;
        attenuation = dAttenuation;
    }
}
