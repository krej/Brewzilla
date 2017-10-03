package beer.unaccpetable.brewzilla.Ingredients;

/**
 * Created by zak on 11/16/2016.
 */

public class Yeast extends Ingredient {
    public String Lab;
    public double Attenuation;

    public Yeast(String sProduct, String sLab,double dAttenuation) {
        name = sProduct;
        Lab = sLab;
        Attenuation = dAttenuation;
    }
}
