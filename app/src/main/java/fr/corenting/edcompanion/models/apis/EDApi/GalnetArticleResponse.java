package fr.corenting.edcompanion.models.apis.EDApi;

import com.google.gson.annotations.SerializedName;

public class GalnetArticleResponse {
    @SerializedName("uri")
    public String Uri;

    @SerializedName("title")
    public String Title;

    @SerializedName("content")
    public String Content;

    @SerializedName("timestamp")
    public long Timestamp;
}
