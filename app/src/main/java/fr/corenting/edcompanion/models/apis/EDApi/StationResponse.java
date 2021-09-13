package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StationResponse {
    @SerializedName("distance_to_star")
    public int DistanceToStar;

    @SerializedName("last_shipyard_update")
    public Date LastShipyardUpdate;

    @SerializedName("max_landing_pad")
    public String MaxLandingPad;

    @SerializedName("name")
    public String Name;

    @SerializedName("is_planetary")
    public boolean IsPlanetary;

    @SerializedName("type")
    public String Type;

    @SerializedName("system")
    public StationSystem System;

    public static class StationSystem {
        @SerializedName("name")
        public String Name;

        @SerializedName("permit_required")
        public boolean PermitRequired;
    }
}
