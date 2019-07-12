package beer.unaccpetable.brewzilla.Models.Responses;

import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Models.Response;

import beer.unaccpetable.brewzilla.Models.RecipeStatistics;

public class RecipeStatsResponse extends Response {
    @Expose
    public RecipeStatistics recipeStats;
    @Expose
    public String idString;

    public RecipeStatsResponse(boolean success, String message) {
        super(success, message);
    }
}
