package beer.unaccpetable.brewzilla;

import org.junit.Assert;
import org.junit.Test;

import beer.unaccpetable.brewzilla.Models.RecipeStatistics;

public class RecipeStatsTests {

    @Test
    public void getPointsTest(){
        RecipeStatistics stats = new RecipeStatistics();
        stats.fg = 1.0375;

        double result = stats.getFgPoints();
        Assert.assertEquals(37.5, result, 0.1);
    }
}
