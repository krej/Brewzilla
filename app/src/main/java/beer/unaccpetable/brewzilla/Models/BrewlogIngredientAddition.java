package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;

import java.time.OffsetDateTime;

public class BrewlogIngredientAddition {
    @Expose
    public String additionGuid;
    @Expose
    public OffsetDateTime time;
    @Expose
    public double amount;
}
