package fr.corenting.edcompanion.models.apis.Frontier;

import com.google.gson.annotations.SerializedName;


public class FrontierProfileResponse {

    @SerializedName("commander")
    public FrontierProfileCommanderResponse Commander;

    @SerializedName("lastSystem")
    public FrontierProfileSystemResponse LastSystem;

    public class FrontierProfileCommanderResponse {
        @SerializedName("name")
        public String Name;

        @SerializedName("credits")
        public long Credits;

        @SerializedName("debt")
        public long Debt;

        @SerializedName("rank")
        public FrontierProfileCommanderRankResponse Rank;
    }

    public class FrontierProfileCommanderRankResponse {
        @SerializedName("combat")
        public int Combat;

        @SerializedName("trade")
        public int Trade;

        @SerializedName("explore")
        public int Explore;

        @SerializedName("crime")
        public int Crime;

        @SerializedName("service")
        public int Service;

        @SerializedName("empire")
        public int Empire;

        @SerializedName("federation")
        public int Federation;

        @SerializedName("power")
        public int Power;

        @SerializedName("cqc")
        public int Cqc;
    }

    public class FrontierProfileSystemResponse {
        @SerializedName("name")
        public String Name;
    }
}