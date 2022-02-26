package fr.corenting.edcompanion.models.apis.EDAPIV4;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class NewsArticleResponse {
    @SerializedName("uri")
    public String Uri;

    @SerializedName("title")
    public String Title;

    @SerializedName("content")
    public String Content;

    @SerializedName("picture")
    public String Picture;

    @SerializedName("published_date")
    public Date PublishedDate;

    public static class SystemHistoryResponse {
        @SerializedName("faction_name")
        public String FactionName;

        @SerializedName("history")
        public List<FactionHistoryResponse> History;
    }

    public static class FactionHistoryResponse {
        @SerializedName("state")
        public String State;

        @SerializedName("updated_at")
        public Date UpdatedAt;

        @SerializedName("influence")
        public float Influence;
    }
}
