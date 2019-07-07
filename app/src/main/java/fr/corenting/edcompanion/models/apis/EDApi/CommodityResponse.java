package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

import fr.corenting.edcompanion.models.NameId;

public class CommodityResponse {
    @SerializedName("average_price")
    public long AveragePrice;

    @SerializedName("name")
    public String Name;

    @SerializedName("id")
    public Long Id;

    @SerializedName("is_rare")
    public Boolean IsRare;

    @SerializedName("category")
    public NameId Category;
}
