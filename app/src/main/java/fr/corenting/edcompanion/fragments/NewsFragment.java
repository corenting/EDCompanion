package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.NewsAdapter;
import fr.corenting.edcompanion.models.events.News;
import fr.corenting.edcompanion.network.NewsNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class NewsFragment extends AbstractListFragment<NewsAdapter> {

    public static final String NEWS_FRAGMENT_TAG = "news_fragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    void getData() {
        NewsNetwork.getNews(getContext());
    }

    @Override
    NewsAdapter getAdapter() {
        return new NewsAdapter(getContext(), recyclerView, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsEvent(News news) {
        // Error case
        if (!news.getSuccess()) {
            endLoading(true);
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(news.getArticles().size() == 0);
        recyclerViewAdapter.submitList(news.getArticles());
    }
}
