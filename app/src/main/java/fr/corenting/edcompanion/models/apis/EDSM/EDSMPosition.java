package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import fr.corenting.edcompanion.models.SystemCoordinates;

public class EDSMPosition extends EDSMBaseResponse {

    @SerializedName("system")
    public String system;

    @SerializedName("systemId")
    public int systemId;

    @SerializedName("firstDiscover")
    public boolean firstDiscover;

    @SerializedName("date")
    public Date visitDate;

    @SerializedName("coordinates")
    public SystemCoordinates coordinates;
}
