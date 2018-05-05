package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

import fr.corenting.edcompanion.models.SystemCoordinates;

public class EDSMSystem {
    @SerializedName("name")
    public String Name;

    @SerializedName("id")
    public long Id;

    @SerializedName("coords")
    public SystemCoordinates Coordinates;
}
