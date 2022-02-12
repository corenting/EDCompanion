package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StationResponse {
    @SerializedName("distance_to_arrival")
    public int DistanceToArrival;

    @SerializedName("last_shipyard_update")
    public Date LastShipyardUpdate;

    @SerializedName("max_landing_pad_size")
    public String MaxLandingPad;

    @SerializedName("name")
    public String Name;

    @SerializedName("is_planetary")
    public boolean IsPlanetary;

    @SerializedName("type")
    public String Type;

    @SerializedName("system_name")
    public String SystemName;

    @SerializedName("system_permit_required")
    public boolean SystemPermitRequired;
}
