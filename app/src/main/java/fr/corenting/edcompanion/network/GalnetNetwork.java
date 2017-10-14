package fr.corenting.edcompanion.network;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.GalnetArticle;
import fr.corenting.edcompanion.models.GalnetNews;

public class GalnetNetwork {
    public static void getNews(Context ctx) {
        Ion.with(ctx)
                .load(ctx.getString(R.string.galnet_rss))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            List<GalnetArticle> articles = new LinkedList<>();
                            for (JsonElement item : result) {
                                JsonObject jsonObject = item.getAsJsonObject();
                                GalnetArticle news = new GalnetArticle();
                                news.setContent(jsonObject.get("content").getAsString().replace("<br />", "\n"));
                                news.setTitle(jsonObject.get("title").getAsString());
                                news.setDateTimestamp(jsonObject.get("timestamp").getAsLong());
                                articles.add(news);
                            }
                            GalnetNews news = new GalnetNews(true, articles);
                            EventBus.getDefault().post(news);
                        } catch (Exception ex) {
                            GalnetNews news = new GalnetNews(false, null);
                            EventBus.getDefault().post(news);
                        }
                    }
                });
    }
}
