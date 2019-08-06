package beer.unaccpetable.brewzilla.Models;


import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by zak on 1/4/2017.
 */

public class Recipe extends ListableObject implements Serializable, Cloneable { //6.5.2018 - I implemented Serializable to be able to pass it through intents. That worked but i'm not sure if it broke anything else

    @Expose
    public String description;
    @Expose
    public RecipeStatistics recipeStats;
    @Expose
    public RecipeParameters recipeParameters;
    @Expose
    public double version;
    @Expose
    public String clonedFrom;
    @Expose
    public String hidden;
    @Expose
    public ArrayList<HopAddition> hops;
    @Expose
    public ArrayList<YeastAddition> yeasts;
    @Expose
    public ArrayList<FermentableAddition> fermentables;
    @Expose
    public ArrayList<AdjunctAddition> adjuncts;
    @Expose
    public Style style;
    @Expose
    public float boilVolume;
    @Expose
    public EquipmentProfile equipmentProfile;
    @Expose
    public String lastModifiedGuid;
    @Expose
    public String createdByUserID;
    @Expose
    public String groupID;
    @Expose
    public boolean isGroupEditable;
    @Expose
    public boolean isPublic;
    @Expose
    public boolean deleted;

    public enum IngredientType {
        Fermntable {
            public String toString() {
                return "fermentable";
            }
        },
        Hop {
            public String toString() {
                return "hop";
            }
        },
        Yeast {
            public String toString() {
                return "yeast";
            }
        },
        Adjunct {
            public String toString() {
                return "adjunct";
            }
        }
    }



    public Recipe() {
        Initiliaze();
    }

    public void Recipe(String sName) {
        name = sName;
    }

    public Recipe(String sName) {
        name = sName;
    }

    public void Initiliaze() {
        if (hops == null) hops = new ArrayList<>();
        if (fermentables == null) fermentables = new ArrayList<>();
        if (yeasts == null) yeasts = new ArrayList<>();
        if (adjuncts == null) adjuncts = new ArrayList<>();
        if (equipmentProfile == null) equipmentProfile = new EquipmentProfile();

        recipeStats = new RecipeStatistics();
        recipeParameters = new RecipeParameters();
    }

    public Recipe clone() throws CloneNotSupportedException {
        return (Recipe) super.clone();
    }
}
