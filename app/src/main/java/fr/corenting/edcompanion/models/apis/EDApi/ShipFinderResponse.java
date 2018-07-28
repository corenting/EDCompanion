package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

public class ShipFinderResponse {
    @SerializedName("distance")
    public float Distance;

    @SerializedName("station")
    public Station Station;
}
