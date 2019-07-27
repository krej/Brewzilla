package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

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
    @Expose
    public double initialStrikeWaterTemp;
    @Expose
    public double initialStrikeWaterVolume;

    public RecipeStatistics() {
        Initialize();
    }

    public void Initialize() {
        abv = 0;
        ibu = 0;
        fg = 0;
        og = 0;
        srm = 0;
        initialStrikeWaterTemp = 70;
        initialStrikeWaterVolume = 0;
    }

    public String getFormattedABV() {
        if (abv == 0) return "0.000";
        return Tools.RoundString(abv, 3);
    }

    public String getFormattedABV(boolean bShowLabel) {
        return getFormattedABV() + "% ABV";
    }

    public String getFormattedIBU() {
        return Tools.RoundString(ibu, 2);
    }

    public String getFormattedFG() {
        return Tools.RoundString(fg, 5);
    }

    public String getFormattedOG() {
        return Tools.RoundString(og, 5);
    }

    public String getFormattedSRM() {
        return Tools.RoundString(srm, 2);
    }

    public String getFormattedStrikeWaterTemp() {
        return Tools.RoundString(initialStrikeWaterTemp, 2);
    }

    public String getFormattedStrikeWaterVolume() {
        return Tools.RoundString(initialStrikeWaterVolume, 2);
    }

    public double getOgPoints() {
        return getPoints(og);
    }

    public double getFgPoints() {
        return getPoints(fg);
    }

    private double getPoints(double value) {
        return (value - 1) * 1000;
    }
}
