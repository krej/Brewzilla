package beer.unaccpetable.brewzilla.Tools;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beer.unaccpetable.brewzilla.Network;

/**
 * Created by Megatron on 10/4/2017.
 */

public class ListableObject {
    public String name = "Empty"; //Used to store 'Empty'
    public String id;
    //protected String ClassName = ""; //Used for the API to know what URL to request to. Could maybe be replaced with just getting the class name later.

    public void Save() {

        //String sRecipeURL = Tools.RestAPIURL() + "/" + ClassName.toLowerCase() + "/";// "/recipe/";
        String sRecipeURL = Tools.RestAPIURL() + "/" + this.getClass().getSimpleName().toLowerCase() + "/";// "/recipe/";
        if ( id != null && id.length() > 0 ) {
            sRecipeURL += id;
        }

        Network.WebRequest(Request.Method.POST, sRecipeURL, BuildRestData());
    }

    protected byte[] BuildRestData() {
        //GsonBuilder gsonBuilder = new GsonBuilder().setExclusionStrategies(new JsonExclusion());
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(this);
        return json.getBytes();
    }
}
