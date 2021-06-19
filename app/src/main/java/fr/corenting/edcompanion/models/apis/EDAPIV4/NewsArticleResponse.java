package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class NewsArticleResponse {
    @SerializedName("uri")
    public String Uri;

    @SerializedName("title")
    public String Title;

    @SerializedName("content")
    public String Content;

    @SerializedName("published_date")
    public Date PublishedDate;
}
