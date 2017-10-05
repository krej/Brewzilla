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
    public List<HopAddition> hops;
    public List<YeastAddition> yeasts;
    public List<FermentableAddition> fermentables;


    public void Recipe(String sName) {
        name = sName;
    }

    public void Recipe(String sName, String sID) {
        Recipe(sName);
        id = sID;
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

}
