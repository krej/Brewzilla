package beer.unaccpetable.brewzilla.Tools;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import beer.unaccpetable.brewzilla.Ingredients.Hop;

/**
 * Created by Megatron on 10/5/2017.
 * This class can be used to exclude fields from being output into Json when using Gson.toJson.
 */

public class JsonExclusion implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
        // return arg0.getDeclaringClass() == Hop.class;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        //return false;
        return f.getName().equals("hop");
    }
}
