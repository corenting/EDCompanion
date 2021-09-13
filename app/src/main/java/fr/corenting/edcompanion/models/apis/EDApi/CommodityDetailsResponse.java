package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import fr.corenting.edcompanion.models.NameId;

public class CommodityDetailsResponse {
    @SerializedName("average_price")
    public long AveragePrice;

    @SerializedName("average_sell_price")
    public double AverageSellPrice;

    @SerializedName("average_buy_price")
    public double AverageBuyPrice;

    @SerializedName("maximum_profit")
    public long MaximumProfit;

    @SerializedName("maximum_sell_price")
    public long MaximumSellPrice;

    @SerializedName("minimum_buy_price")
    public long MinimumBuyPrice;

    @SerializedName("name")
    public String Name;

    @SerializedName("id")
    public Long Id;

    @SerializedName("is_rare")
    public Boolean IsRare;

    @SerializedName("category")
    public NameId Category;

    @SerializedName("maximum_sellers")
    public List<CommodityDetailsStationResponse> MaximumSellers;

    @SerializedName("minimum_buyers")
    public List<CommodityDetailsStationResponse> MinimumBuyers;

    public static class CommodityDetailsStationResponse {

        @SerializedName("collected_at")
        public Date CollectedAt;

        @SerializedName("demand")
        public long Demand;

        @SerializedName("supply")
        public long Supply;

        @SerializedName("buy_price")
        public long BuyPrice;

        @SerializedName("sell_price")
        public long SellPrice;

        @SerializedName("price_difference_percentage")
        public int PriceDifferencePercentage;

        @SerializedName("station")
        public StationResponse Station;
    }
}
