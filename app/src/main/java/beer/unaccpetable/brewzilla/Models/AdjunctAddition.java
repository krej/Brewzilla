package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by Megatron on 10/25/2017.
 */

public class AdjunctAddition extends IngredientAddition {
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

    public AdjunctAddition() {

    }

    public AdjunctAddition(Adjunct a) {
        super();
        adjunct = a;
        name = a.name;
        amount = 0;
        unit = "";
        time = 0;
        timeUnit = "";
        type = "";
    }
}
