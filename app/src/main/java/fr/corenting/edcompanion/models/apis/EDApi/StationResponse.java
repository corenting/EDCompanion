package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

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

    @SerializedName("ships_sold")
    public List<String> ShipsSold;

    @SerializedName("system")
    public StationSystem System;

    public class StationSystem {
        @SerializedName("name")
        public String Name;

        @SerializedName("permit_required")
        public boolean PermitRequired;
    }
}
