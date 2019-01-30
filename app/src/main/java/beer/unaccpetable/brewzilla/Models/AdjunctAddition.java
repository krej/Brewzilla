package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by Megatron on 10/25/2017.
 */

public class AdjunctAddition extends ListableObject {
    @Expose
    public String adjunctID;
    @Expose
    public double amount;
    @Expose
    public String unit;
    @Expose
    public double time;
    @Expose
    public String timeUnit;
    @Expose
    public String type;
    @Expose
    public Adjunct adjunct;
}
