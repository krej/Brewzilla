package beer.unaccpetable.brewzilla.Tools;

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
}
