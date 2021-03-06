package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FactionHistoryResponse {
    @SerializedName("system")
    public String System;

    @SerializedName("state")
    public String State;

    @SerializedName("updated_at")
    public Date UpdatedAt;

    @SerializedName("influence")
    public float Influence;
}
