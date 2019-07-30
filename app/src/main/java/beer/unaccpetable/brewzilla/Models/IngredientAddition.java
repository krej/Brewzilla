package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import java.util.UUID;

/**
 * Created by zak on 11/16/2016.
 */

public class IngredientAddition extends ListableObject
{
    @Expose
    public UUID additionGuid;

    public IngredientAddition() {
        additionGuid = java.util.UUID.randomUUID();
    }

    //public String name = "Empty";
    /*public IngredientAddition(String sName) {
        Name = sName;
    }*/

}
