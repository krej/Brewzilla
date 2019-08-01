package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Models.Yeast;

public class RecipeEditorViewModel {
    @Expose
    public Recipe Recipe;
    @Expose
    public ArrayList<Hop> Hops;
    @Expose
    public ArrayList<Fermentable> Fermentables;
    @Expose
    public ArrayList<Yeast> Yeasts;
    @Expose
    ArrayList<Style> Styles;
}
