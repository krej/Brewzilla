package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;

public class BrewLog extends ListableObject implements Serializable {
    @Expose
    public Recipe originalRecipe;
    @Expose
    public Recipe rectifiedRecipe;

    @Expose
    public String recipeIdString;

    @Expose
    public String mashStartTime;
    @Expose
    public String mashEndTime;

    @Expose
    public boolean vaurloff;

    @Expose
    public String spargeStartTime;
    @Expose
    public String spargeEndTime;

    @Expose
    public String preBoilVolumeEstimate;
    @Expose
    public double preBoilVolumeActual;

    @Expose
    public String boilStartTime;
    @Expose
    public String boilEndTime;

    @Expose
    public ArrayList<BrewlogIngredientAddition> actualHopAdditions;
    @Expose
    public ArrayList<BrewlogIngredientAddition> actualAdjunctAdditions;

    @Expose
    public double og;
    @Expose
    public double fg;

    @Expose
    public String actualBatchSizeString;
    @Expose
    public double actualBatchSize;

    @Expose
    public String lastModifiedGuid;

    public BrewLog() {
        idString = "";
    }
}
