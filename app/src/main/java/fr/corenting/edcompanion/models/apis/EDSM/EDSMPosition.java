package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

public class EDSMPosition extends EDSMBaseResponse {

    @SerializedName("system")
    public String system;

    @SerializedName("systemId")
    public int systemId;

    @SerializedName("firstDiscover")
    public boolean firstDiscover;
}
