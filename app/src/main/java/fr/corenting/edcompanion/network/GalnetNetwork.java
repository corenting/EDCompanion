package fr.corenting.edcompanion.network;

import android.content.Context;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Callback;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.GalnetArticle;
import fr.corenting.edcompanion.models.GalnetNews;

public class GalnetNetwork {
    public static void getNews(Context ctx) {
        Bridge.get(ctx.getString(R.string.galnet_rss))
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        try {
                            if (e != null) {
                                throw new Exception();
                            }
                            JsonArray json = new JsonParser().parse(response.asString()).getAsJsonArray();

                            List<GalnetArticle> articles = new LinkedList<>();
                            for (JsonElement item : json) {
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
