package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class CommunityGoalsResponse {
    @SerializedName("contributors")
    public long Contributors;

    @SerializedName("date")
    public CommunityGoalsDate Date;

    @SerializedName("description")
    public String Description;

    @SerializedName("id")
    public long Id;

    @SerializedName("location")
    public CommunityGoalsLocation Location;

    @SerializedName("objective")
    public String Objective;

    @SerializedName("ongoing")
    public boolean Ongoing;

    @SerializedName("reward")
    public String Reward;

    @SerializedName("rewards")
    public List<CommunityGoalsRewards> Rewards;

    @SerializedName("title")
    public String Title;

    @SerializedName("tier_progress")
    public CommunityGoalTierProgress TierProgress;

    public static class CommunityGoalsDate {
        @SerializedName("end")
        public Date End;

        @SerializedName("last_update")
        public Date LastUpdate;
    }

    public static class CommunityGoalsLocation {
        @SerializedName("station")
        public String Station;

        @SerializedName("system")
        public String System;
    }

    public static class CommunityGoalTierProgress {
        @SerializedName("current")
        public int Current;

        @SerializedName("total")
        public int Total;
    }

    public static class CommunityGoalsRewards {
        @SerializedName("contributors")
        public String Contributors;

        @SerializedName("reward")
        public String Reward;

        @SerializedName("tier")
        public String Tier;
    }
}
