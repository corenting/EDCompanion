package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class DistanceResponse {
    @SerializedName("distance")
    public float Distance;

    @SerializedName("from")
    public DistanceSystem FromSystem;

    @SerializedName("to")
    public DistanceSystem ToSystem;

    public class DistanceSystem {
        @SerializedName("x")
        public float X;

        @SerializedName("y")
        public float Y;

        @SerializedName("z")
        public float Z;

        @SerializedName("id")
        public long Id;

        @SerializedName("name")
        public String Name;

        @SerializedName("permit_required")
        public boolean PermitRequired;
    }
}
