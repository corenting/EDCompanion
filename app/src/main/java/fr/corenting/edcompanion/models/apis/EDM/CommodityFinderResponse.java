package fr.corenting.edcompanion.models.apis.EDM;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommodityFinderResponse {
    @SerializedName("commodity_search")
    public String CommoditySearch;

    @SerializedName("max_radius")
    public int MaxRadius;

    @SerializedName("min_stock")
    public int MinStock;

    @SerializedName("min_pad")
    public String MinimumPad;

    @SerializedName("reference_system")
    public String ReferenceSystem;

    @SerializedName("sellers")
    public List<CommodityFinderItem> Sellers;

    public class CommodityFinderItem {
        @SerializedName("buy")
        public long BuyPrice;

        @SerializedName("pad")
        public String LandingPad;

        @SerializedName("station")
        public String Station;

        @SerializedName("system")
        public String System;

        @SerializedName("stock")
        public long Stock;
    }
}
