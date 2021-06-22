package fr.corenting.edcompanion.network.retrofit;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDAPIV4.NewsArticleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EDApiV4Retrofit {
    @GET("galnet")
    Call<List<NewsArticleResponse>> getGalnetNews(@Query("lang") String language);

    @GET("news")
    Call<List<NewsArticleResponse>> getNews(@Query("lang") String language);
}
