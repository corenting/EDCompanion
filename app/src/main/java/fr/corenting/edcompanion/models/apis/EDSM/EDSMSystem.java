package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

public class EDSMSystem {
    @SerializedName("name")
    public String Name;

    @SerializedName("id")
    public long Id;

    @SerializedName("requirePermit")
    public boolean PermitRequired;

    @SerializedName("information")
    public EDSMSystemInformation Information;
}
