package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SystemHistoryResponse {
    @SerializedName("name")
    public String Name;

    @SerializedName("history")
    public List<FactionHistoryResponse> History;
}
