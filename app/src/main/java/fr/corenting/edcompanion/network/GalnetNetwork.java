package fr.corenting.edcompanion.network;

import android.content.Context;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.GalnetArticle;
import fr.corenting.edcompanion.models.apis.EDApi.GalnetArticleResponse;
import fr.corenting.edcompanion.models.events.GalnetNews;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;

public class GalnetNetwork {
    public static void getNews(Context ctx) {

        EDApiRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());

        retrofit2.Callback<List<GalnetArticleResponse>> callback = new retrofit2.Callback<List<GalnetArticleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GalnetArticleResponse>> call,
                                   retrofit2.Response<List<GalnetArticleResponse>> response) {
                List<GalnetArticleResponse> body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    GalnetNews news;
                    try {
                        List<GalnetArticle> articles = new ArrayList<>();
                        for (GalnetArticleResponse item : body) {
                            articles.add(GalnetArticle.Companion.fromGalnetArticleResponse(item));
                        }
                        news = new GalnetNews(true, articles);
                    } catch (Exception e) {
                        news = new GalnetNews(false, new ArrayList<GalnetArticle>());
                    }
                    EventBus.getDefault().post(news);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GalnetArticleResponse>> call,
                                  @NonNull Throwable t) {
                GalnetNews news = new GalnetNews(false, new ArrayList<GalnetArticle>());
                EventBus.getDefault().post(news);
            }
        };
        retrofit.getGalnetNews().enqueue(callback);
    }
}
