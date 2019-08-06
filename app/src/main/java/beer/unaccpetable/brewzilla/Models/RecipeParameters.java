package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Megatron on 10/25/2017.
 */

public class RecipeParameters implements Serializable {
    @Expose
    public String ibuCalcType;
    @Expose
    public String fermentableCalcType;
    @Expose
    public double ibuBoilTimeCurveFit;
    @Expose
    public double gristRatio;
    @Expose
    public double initialMashTemp;
    @Expose
    public double targetMashTemp;

    public RecipeParameters() {
        Initialize();
    }

    public void Initialize() {
        ibuCalcType = "basic";
        fermentableCalcType = "basic";
        ibuBoilTimeCurveFit = -0.04;
        //intoFermenterVolume = 5;
        gristRatio = 1.5;
        initialMashTemp = 70;
        targetMashTemp = 150;
    }
}
