package fr.corenting.edcompanion.network;

import android.support.design.widget.Snackbar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.GalnetFragment;
import fr.corenting.edcompanion.models.GalnetNews;

public class GalnetNetwork {
    public static void getNews(final GalnetFragment fragment) {
        Ion.with(fragment)
                .load(fragment.getString(R.string.galnet_rss))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            List<GalnetNews> res = new LinkedList<>();
                            for (JsonElement item : result) {
                                JsonObject jsonObject = item.getAsJsonObject();
                                GalnetNews news = new GalnetNews();
                                news.setContent(jsonObject.get("content").getAsString().replace("<br />", "\n"));
                                news.setTitle(jsonObject.get("title").getAsString());
                                news.setDateTimestamp(jsonObject.get("timestamp").getAsLong());
                                res.add(news);
                            }
                            EventBus.getDefault().post(res);
                            fragment.endLoading(res.size());
                        } catch (Exception ex) {
                            fragment.endLoading(0);
                            Snackbar snackbar = Snackbar
                                    .make(fragment.getActivity().findViewById(android.R.id.content),
                                            R.string.download_error, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }
}
