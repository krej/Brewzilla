package beer.unaccpetable.brewzilla.Models.Responses;

import com.google.gson.annotations.Expose;

public class LastModifiedGuidResponse {
    @Expose
    public String lastModifiedGuid;
    @Expose
    public boolean Success;
    @Expose
    public String idString;
    @Expose
    public String Message;
}
