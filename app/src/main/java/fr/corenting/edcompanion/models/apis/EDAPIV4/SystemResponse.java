package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SystemResponse {
    @SerializedName("allegiance")
    public String Allegiance;

    @SerializedName("controlling_faction")
    public String ControllingFaction;

    @SerializedName("government")
    public String Government;


    @SerializedName("name")
    public String Name;

    @SerializedName("x")
    public float X;

    @SerializedName("y")
    public float Y;

    @SerializedName("z")
    public float Z;

    @SerializedName("permit_required")
    public boolean PermitRequired;

    @SerializedName("state")
    public String State;

    @SerializedName("population")
    public long Population;

    @SerializedName("security")
    public String Security;

    @SerializedName("primary_economy")
    public String PrimaryEconomy;

    @SerializedName("power")
    public String Power;

    @SerializedName("power_state")
    public String PowerState;

    @SerializedName("factions")
    public List<SystemFactionResponse> Factions;

    public static class SystemFactionResponse {
        @SerializedName("name")
        public String Name;

        @SerializedName("state")
        public String State;

        @SerializedName("government")
        public String Government;

        @SerializedName("allegiance")
        public String Allegiance;

        @SerializedName("updated_at")
        public Date UpdatedAt;

        @SerializedName("influence")
        public float Influence;

        @SerializedName("happiness")
        public String Happiness;


    }
}
