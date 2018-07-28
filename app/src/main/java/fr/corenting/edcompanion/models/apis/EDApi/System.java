package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

public class System {
    @SerializedName("name")
    public String Name;

    @SerializedName("permit_required")
    public boolean PermitRequired;
}
