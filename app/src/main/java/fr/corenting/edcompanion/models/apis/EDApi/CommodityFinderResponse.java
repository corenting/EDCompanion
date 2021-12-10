package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CommodityFinderResponse {

    @SerializedName("buy_price")
    public long BuyPrice;

    @SerializedName("sell_price")
    public long SellPrice;

    @SerializedName("distance")
    public float Distance;

    @SerializedName("distance_to_star")
    public int DistanceToStar;

    @SerializedName("station")
    public StationResponse Station;

    @SerializedName("supply")
    public long Stock;

    @SerializedName("demand")
    public long Demand;

    @SerializedName("last_price_update")
    public Date LastPriceUpdate;

    @SerializedName("price_difference_percentage")
    public int PriceDifferencePercentage;

    public static class StationResponse {
        @SerializedName("distance_to_star")
        public int DistanceToStar;

        @SerializedName("last_shipyard_update")
        public Date LastShipyardUpdate;

        @SerializedName("max_landing_pad")
        public String MaxLandingPad;

        @SerializedName("name")
        public String Name;

        @SerializedName("is_planetary")
        public boolean IsPlanetary;

        @SerializedName("type")
        public String Type;

        @SerializedName("system")
        public StationSystem System;

        public static class StationSystem {
            @SerializedName("name")
            public String Name;

            @SerializedName("permit_required")
            public boolean PermitRequired;
        }
    }

}