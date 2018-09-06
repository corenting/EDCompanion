package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SystemResponse {
    @SerializedName("name")
    public String Name;

    @SerializedName("permit_required")
    public boolean PermitRequired;

    @SerializedName("allegiance")
    public String Allegiance;

    @SerializedName("controlling_faction_name")
    public String ControllingFaction;

    @SerializedName("government")
    public String Government;

    @SerializedName("population")
    public long Population;

    @SerializedName("power")
    public String Power;

    @SerializedName("power_state")
    public String PowerState;

    @SerializedName("primary_economy")
    public String PrimaryEconomy;

    @SerializedName("security")
    public String Security;

    @SerializedName("state")
    public String State;

    @SerializedName("updated_at")
    public Date UpdatedAt;

    @SerializedName("x")
    public float X;

    @SerializedName("y")
    public float Y;

    @SerializedName("z")
    public float Z;

    @SerializedName("factions")
    public List<FactionResponse> Factions;
}
