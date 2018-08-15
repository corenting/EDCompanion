package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CommodityFinderResponse {

    @SerializedName("buy_price")
    public long BuyPrice;

    @SerializedName("distance")
    public float Distance;

    @SerializedName("distance_to_star")
    public int DistanceToStar;

    @SerializedName("station")
    public Station Station;

    @SerializedName("supply")
    public long Stock;

    @SerializedName("last_price_update")
    public Date LastPriceUpdate;
}