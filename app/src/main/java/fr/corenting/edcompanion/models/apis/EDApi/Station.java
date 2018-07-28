package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Station {
    @SerializedName("distance_to_star")
    public int DistanceToStar;

    @SerializedName("last_shipyard_update")
    public Date LastShipyardUpdate;

    @SerializedName("max_landing_pad")
    public String MaxLandingPad;

    @SerializedName("name")
    public String Name;

    @SerializedName("type")
    public String Type;

    @SerializedName("system")
    public System System;
}
