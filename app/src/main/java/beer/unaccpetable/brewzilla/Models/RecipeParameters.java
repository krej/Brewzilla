package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

/**
 * Created by Megatron on 10/25/2017.
 */

public class RecipeParameters {
    @Expose
    public String ibuCalcType;
    @Expose
    public String fermentableCalcType;
    @Expose
    public double ibuBoilTimeCurveFit;
    @Expose
    public double intoFermenterVolume;
}
