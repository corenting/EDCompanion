package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ShipFinderResponse {
    @SerializedName("distance")
    public float Distance;

    @SerializedName("station")
    public ShipFinderStation Station;

    public class ShipFinderStation {
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
        public ShipFinderSystem System;

        public class ShipFinderSystem {
            @SerializedName("name")
            public String Name;

            @SerializedName("permit_required")
            public boolean PermitRequired;
        }
    }
}
