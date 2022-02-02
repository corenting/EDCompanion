package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SystemHistoryResponse {
    @SerializedName("faction_name")
    public String FactionName;

    @SerializedName("history")
    public List<FactionHistoryResponse> History;
}
