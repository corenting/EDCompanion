package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ShipFinderResponse {
    @SerializedName("distance_from_reference_system")
    public float DistanceFromReferenceSystem;

    @SerializedName("distance_to_arrival")
    public float DistanceToArrival;

    @SerializedName("is_fleet_carrier")
    public boolean IsFleetCarrier;

    @SerializedName("is_planetary")
    public boolean IsPlanetary;

    @SerializedName("is_settlement")
    public boolean IsSettlement;

    @SerializedName("max_landing_pad_size")
    public String MaxLandingPadSize;

    @SerializedName("name")
    public String Name;

    @SerializedName("system_name")
    public String SystemName;

    @SerializedName("shipyard_updated_at")
    public Date ShipyardUpdatedAt;

}
