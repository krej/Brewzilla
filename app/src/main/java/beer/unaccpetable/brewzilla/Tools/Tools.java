package beer.unaccpetable.brewzilla.Tools;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import beer.unaccpetable.brewzilla.Ingredients.Recipe;

/**
 * Created by zak on 11/16/2016.
 */

public class Tools {

    public static double ParseDouble(String d) {
        if (d.length() == 0 ) return 0;
        return Double.parseDouble(d);
    }
    public static int ParseInt(String d) {
        if (d.length() == 0 ) return 0;
        return Integer.parseInt(d);
    }

    public static void ShowToast(Context c, CharSequence text, int length) {
        Toast t = Toast.makeText(c, text, length);
        t.show();
    }

    //TODO: I don't really like having these here but they work for now...
    public static String RestAPIURL() {
        return "http://rest.unacceptable.beer:2403";
    }

    public static String SanitizeDeploydJSON(String response) {
        String json = response;
        json = json.replace("[", "[\n");
        json = json.replace("]", "]\n");
        json = json.replace("},", "}\n");
        return json;
    }

    public static ArrayList<JSONObject> GetJSONObjects(String json) {
        ArrayList<JSONObject> objs = new ArrayList<JSONObject>();
        json = Tools.SanitizeDeploydJSON(json);
        try (StringReader sr = new StringReader(json); BufferedReader in = new BufferedReader(sr)) {
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    if (line.equals("[") || line.equals("]")) continue;
                    JSONObject object = new JSONObject(line);
                    objs.add(object);
                }
            } catch (JSONException ex) {

            }
        } catch(IOException e) {

        }

        return objs;
    }
}
