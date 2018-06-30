package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class CommunityGoalsResponse {
    @SerializedName("goals")
    public List<CommunityGoalsItemResponse> Goals;

    public class CommunityGoalsItemResponse {
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

        public class CommunityGoalsDate {
            @SerializedName("end")
            public Date End;

            @SerializedName("last_update")
            public Date LastUpdate;
        }

        public class CommunityGoalsLocation {
            @SerializedName("station")
            public String Station;

            @SerializedName("system")
            public String System;
        }

        public class CommunityGoalTierProgress {
            @SerializedName("current")
            public int Current;

            @SerializedName("total")
            public int Total;
        }

        public class CommunityGoalsRewards {
            @SerializedName("contributorss")
            public String Contributors;

            @SerializedName("rewards")
            public String Rewards;

            @SerializedName("tiers")
            public String Tier;
        }
    }
}
