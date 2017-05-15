package fr.corenting.edcompanion.network;

import android.support.design.widget.Snackbar;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.GalnetFragment;
import fr.corenting.edcompanion.models.GalnetNews;

public class GalnetNetwork {
    public static void getNews(final GalnetFragment fragment) {
        Ion.with(fragment)
                .load(fragment.getString(R.string.galnet_rss))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }
                            Feed feed = EarlParser.parseOrThrow(new ByteArrayInputStream(result.getBytes("UTF-8")), Integer.MAX_VALUE);
                            for (Item i : feed.getItems()) {
                                GalnetNews news = new GalnetNews();
                                news.setContent(i.getDescription().replace("<br />", ""));
                                news.setTitle(i.getTitle());
                                EventBus.getDefault().post(news);
                            }
                            fragment.endLoading(feed.getItems().size());
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
