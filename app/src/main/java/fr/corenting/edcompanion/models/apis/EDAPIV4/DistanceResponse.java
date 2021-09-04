package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

public class DistanceResponse {
    @SerializedName("distance_in_ly")
    public float DistanceInLy;

    @SerializedName("first_system")
    public DistanceSystem FirstSystem;

    @SerializedName("second_system")
    public DistanceSystem SecondSystem;

    public static class DistanceSystem {
        @SerializedName("name")
        public String Name;

        @SerializedName("permit_required")
        public boolean PermitRequired;

        @SerializedName("x")
        public float X;

        @SerializedName("y")
        public float Y;

        @SerializedName("z")
        public float Z;
    }
}
