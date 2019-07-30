package beer.unaccpetable.brewzilla.Models;

import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

public class EquipmentProfile extends ListableObject {
    @Expose
    public String createdByUserId;
    @Expose
    public float boilSize;
    @Expose
    public float intoFermenterVolume;
    @Expose
    public float efficiency;
    @Expose
    public float batchSize;

    public EquipmentProfile() {
        boilSize = 6;
        intoFermenterVolume = 5.5f;
        efficiency = 75;
        batchSize = 0; //TODO: what was this?
    }
}
