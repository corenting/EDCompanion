package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

public class EDSMRanksResponse extends EDSMBaseResponse {
    @SerializedName("ranks")
    public EDSMInnerRanks ranks;

    @SerializedName("progress")
    public EDSMInnerRanks progress;

    @SerializedName("ranksVerbose")
    public EDSMInnerRanksNames ranksNames;

    public static class EDSMInnerRanksNames {
        @SerializedName("Combat")
        public String combat;

        @SerializedName("Trade")
        public String trade;

        @SerializedName("Explore")
        public String explore;

        @SerializedName("CQC")
        public String cqc;

        @SerializedName("Soldier")
        public String mercenary;

        @SerializedName("Exobiologist")
        public String exobiologist;

        @SerializedName("Federation")
        public String federation;

        @SerializedName("Empire")
        public String empire;
    }

    public static class EDSMInnerRanks {
        @SerializedName("Combat")
        public int combat;

        @SerializedName("Trade")
        public int trade;

        @SerializedName("Explore")
        public int explore;

        @SerializedName("CQC")
        public int cqc;

        @SerializedName("Soldier")
        public int mercenary;

        @SerializedName("Exobiologist")
        public int exobiologist;

        @SerializedName("Federation")
        public int federation;

        @SerializedName("Empire")
        public int empire;
    }
}
