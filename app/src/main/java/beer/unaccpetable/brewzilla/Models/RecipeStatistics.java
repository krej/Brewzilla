package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

/**
 * Created by Megatron on 10/25/2017.
 */

public class RecipeStatistics {
    @Expose
    public double abv;
    @Expose
    public double ibu;
    @Expose
    public double fg;
    @Expose
    public double og;
    @Expose
    public double srm;
}
