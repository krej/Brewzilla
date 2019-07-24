package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

public class Style extends ListableObject {

    @Expose
    public String category;
    @Expose
    public String description;
    @Expose
    public String profile;
    @Expose
    public String ingredients;
    @Expose
    public String examples;

    //Stats Ranges

    @Expose
    public double minOG;
    @Expose
    public double maxOG;
    @Expose
    public double minFG;
    @Expose
    public double maxFG;
    @Expose
    public double minIBU;
    @Expose
    public double maxIBU;
    @Expose
    public double minCarb;
    @Expose
    public double maxCarb;
    @Expose
    public double minColor;
    @Expose
    public double maxColor;
    @Expose
    public double minABV;
    @Expose
    public double maxABV;
}
