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
}