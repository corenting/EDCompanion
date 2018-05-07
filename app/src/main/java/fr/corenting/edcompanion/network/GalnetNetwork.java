package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.models.GalnetArticle;
import fr.corenting.edcompanion.models.GalnetNews;
import fr.corenting.edcompanion.models.apis.EDApi.GalnetArticleResponse;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;

public class GalnetNetwork {
    public static void getNews(Context ctx) {

        EDApiRetrofit retrofit = RetrofitUtils.getEdApiRetrofit(ctx);

        retrofit2.Callback<List<GalnetArticleResponse>> callback = new retrofit2.Callback<List<GalnetArticleResponse>>() {
            @Override
            public void onResponse(Call<List<GalnetArticleResponse>> call, retrofit2.Response<List<GalnetArticleResponse>> response) {
                List<GalnetArticleResponse> body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    GalnetNews news;
                    try {
                        List<GalnetArticle> articles = new LinkedList<>();
                        for (GalnetArticleResponse item : body) {
                            GalnetArticle newArticle = new GalnetArticle();
                            newArticle.setContent(item.Content.replace("<br />", "\n"));
                            newArticle.setTitle(item.Title);
                            newArticle.setDateTimestamp(item.Timestamp);
                            articles.add(newArticle);
                        }
                        news = new GalnetNews(true, articles);
                    } catch (Exception e) {
                        news = new GalnetNews(false, null);
                    }
                    EventBus.getDefault().post(news);
                }
            }

            @Override
            public void onFailure(Call<List<GalnetArticleResponse>> call, Throwable t) {
                GalnetNews news = new GalnetNews(false, null);
                EventBus.getDefault().post(news);
            }
        };
        retrofit.getGalnetNews().enqueue(callback);
    }
}
