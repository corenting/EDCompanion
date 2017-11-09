package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.GalnetAdapter;
import fr.corenting.edcompanion.models.GalnetArticle;
import fr.corenting.edcompanion.models.GalnetNews;
import fr.corenting.edcompanion.network.GalnetNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class GalnetFragment extends Fragment {

    public static final String GALNET_FRAGMENT_TAG = "galnet_fragment";
    public static final String GALNET_REPORTS_FRAGMENT_TAG = "galnet_reports_fragment";

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.emptySwipe)
    public SwipeRefreshLayout emptySwipeRefreshLayout;
    private boolean reportsMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, v);

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                endLoading(0);
                emptySwipeRefreshLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                GalnetNetwork.getNews(getContext());
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        emptySwipeRefreshLayout.setOnRefreshListener(listener);

        // Check if reports only or not
        reportsMode = getArguments().getBoolean("reportsMode", false);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup views
        emptySwipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        // Register event and get the news
        EventBus.getDefault().register(this);
        GalnetNetwork.getNews(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onNewsEvent(GalnetNews news) {
        stopSwipeToRefresh();
        if (!news.Success) {
            endLoading(0);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }
        GalnetNews copy = new GalnetNews(true, new LinkedList<GalnetArticle>());

        int count = 0;
        for (GalnetArticle n : news.Articles) {
            boolean isReport = n.getTitle().matches(".*(Weekly).*(Report).*") ||
                    n.getTitle().contains("Starport Status Update");

            // Add the article or not depending on the mode and the title
            if ((reportsMode && isReport) || (!reportsMode && !isReport)) {
                copy.Articles.add(n);
                count++;
            }
        }
        GalnetAdapter adapter =  new GalnetAdapter(getContext(), recyclerView, copy.Articles, false);
        recyclerView.setAdapter(adapter);
        endLoading(count);
    }

    private void endLoading(int count) {
        if (count <= 0) {
            emptySwipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void stopSwipeToRefresh() {
        emptySwipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setRefreshing(false);
    }
}
