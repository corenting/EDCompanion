package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CommunityGoalsResponse {
    @SerializedName("contributors")
    public long Contributors;

    @SerializedName("last_update")
    public Date LastUpdate;

    @SerializedName("end_date")
    public Date EndDate;

    @SerializedName("description")
    public String Description;

    @SerializedName("id")
    public long Id;

    @SerializedName("station")
    public String Station;

    @SerializedName("system")
    public String System;

    @SerializedName("objective")
    public String Objective;

    @SerializedName("ongoing")
    public boolean Ongoing;

    @SerializedName("reward")
    public String Reward;

    @SerializedName("title")
    public String Title;

    @SerializedName("current_tier")
    public int CurrentTier;

    @SerializedName("max_tier")
    public int MaxTier;
}
