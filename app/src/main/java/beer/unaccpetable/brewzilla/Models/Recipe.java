package beer.unaccpetable.brewzilla.Models;


import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Tools.ListableObject;

/**
 * Created by zak on 1/4/2017.
 */

public class Recipe extends ListableObject implements Serializable { //6.5.2018 - I implemented Serializable to be able to pass it through intents. That worked but i'm not sure if it broke anything else
    @Expose
    public String style;
    @Expose
    public String description;
    @Expose
    public RecipeStatistics recipeStats;
    @Expose
    public RecipeParameters recipeParameters;
    @Expose
    public double version;
    @Expose
    public String parentRecipe;
    @Expose
    public String clonedFrom;
    @Expose
    public String hidden;
    @Expose
    public ArrayList<HopAddition> hops;
    @Expose
    public ArrayList<Yeast> yeasts;
    @Expose
    public ArrayList<FermentableAddition> fermentables;
    @Expose
    public ArrayList<AdjunctAddition> adjuncts;
    @Expose
    public String styleID;


    public Recipe() {}

    public void Recipe(String sName) {
        name = sName;
    }

    public Recipe(String sName, String sStyle) {
        name = sName;
        style = sStyle;
    }


    public void Save() {
        super.Save();
/*
        for (HopAddition h : hops) {
            h.Save();
        }

        for (FermentableAddition f : fermentables) {
            f.Save();
        }

        //Yeasts currently only saves the yeastID which can't be saved.
        for (YeastAddition y : yeasts) {
            y.Save();
        }*/
    }

    public void Initiliaze() {
        if (hops == null) hops = new ArrayList<>();
        if (fermentables == null) fermentables = new ArrayList<>();
        if (yeasts == null) yeasts = new ArrayList<>();
    }

    /*
    For Some reason I can't convert ArrayList<HopAddition> to ArrayList<ListableObject> so I need to clear and readd these...
     */
    public void ClearIngredients() {
        hops.clear();
        fermentables.clear();
        yeasts.clear();
    }

    public void PopulateHops(ArrayList<ListableObject> hops) {
        for (int i = 0; i < hops.size(); i++) {
            HopAddition h = (HopAddition)hops.get(i);
            this.hops.add(h);
        }
    }
    public void PopulateFermentables(ArrayList<ListableObject> fermentables) {
        for (int i = 0; i < fermentables.size(); i++) {
            FermentableAddition h = (FermentableAddition) fermentables.get(i);
            this.fermentables.add(h);
        }
    }
    public void PopulateYeasts(ArrayList<ListableObject> yeasts) {
        for (int i = 0; i < yeasts.size(); i++) {
            Yeast h = (Yeast) yeasts.get(i);
            this.yeasts.add(h);
        }
    }
}
