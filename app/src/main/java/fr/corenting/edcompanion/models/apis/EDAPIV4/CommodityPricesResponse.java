package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

import fr.corenting.edcompanion.models.NameId;

public class CommodityPricesResponse {
    @SerializedName("average_buy_price")
    public long AverageBuyPrice;

    @SerializedName("average_sell_price")
    public long AverageSellPrice;

    @SerializedName("commodity")
    public CommodityPricesDetailsResponse Commodity;


    public static class CommodityPricesDetailsResponse {
        @SerializedName("name")
        public String Name;

        @SerializedName("id")
        public Long Id;

        @SerializedName("is_rare")
        public Boolean IsRare;

        @SerializedName("category")
        public String Category;
    }
}
