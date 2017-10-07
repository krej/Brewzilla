package beer.unaccpetable.brewzilla.Ingredients;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import beer.unaccpetable.brewzilla.Network;
import beer.unaccpetable.brewzilla.Tools.ListableObject;
import beer.unaccpetable.brewzilla.Tools.Tools;

/**
 * Created by zak on 1/4/2017.
 */

public class Recipe extends ListableObject {
    @Expose
    public String style;
    @Expose
    public String description;
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
    public double version;
    public String test;
    public ArrayList<HopAddition> hops;
    public ArrayList<YeastAddition> yeasts;
    public ArrayList<FermentableAddition> fermentables;


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

        for (HopAddition h : hops) {
            h.Save();
        }

        for (FermentableAddition f : fermentables) {
            f.Save();
        }

        //Yeasts currently only saves the yeastID which can't be saved.
        for (YeastAddition y : yeasts) {
            y.Save();
        }
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
            YeastAddition h = (YeastAddition) yeasts.get(i);
            this.yeasts.add(h);
        }
    }
}
