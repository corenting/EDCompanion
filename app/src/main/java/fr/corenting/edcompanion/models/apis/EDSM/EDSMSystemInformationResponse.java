package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

public class EDSMSystemInformationResponse {
    @SerializedName("allegiance")
    public String Allegiance;

    @SerializedName("security")
    public String Security;

    @SerializedName("government")
    public String Government;

    @SerializedName("economy")
    public String Economy;
}
