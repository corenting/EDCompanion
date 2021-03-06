package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FactionResponse {
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
