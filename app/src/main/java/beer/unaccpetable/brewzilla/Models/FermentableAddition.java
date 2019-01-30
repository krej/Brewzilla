package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by Megatron on 9/25/2017.
 */

public class FermentableAddition extends ListableObject {
    @Expose
    public String fermentableID;
    @Expose
    public String use = "Mash"; //Hard coded to Mash for now because I dont know the purpose of this
    @Expose
    public double weight;
    @Expose
    public Fermentable fermentable;

    public FermentableAddition(String sName, double dWeight, double dPPG, int iColor) {
        fermentable = new Fermentable(sName, dPPG, iColor);
        name = sName;
        weight = dWeight;
    }

    public String name() {
        return fermentable.name;
    }
}
