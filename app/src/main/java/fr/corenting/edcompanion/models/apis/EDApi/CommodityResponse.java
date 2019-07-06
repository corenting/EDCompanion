package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

public class CommodityResponse {
    @SerializedName("average_price")
    public long AveragePrice;

    @SerializedName("name")
    public String Name;


    @SerializedName("id")
    public Long Id;
}
