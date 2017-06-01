package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.models.Ranks;
import fr.corenting.edcompanion.network.StatusNetwork;

public class StatusFragment extends Fragment  {

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.creditsTextView)
    public TextView creditsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        ButterKnife.bind(this, v);


        //Swipe to refresh setup
        final StatusFragment parent = this;
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                StatusNetwork.getAll(parent);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);

        creditsTextView.setText("TEST");
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup views
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        // Register event and get the news
        EventBus.getDefault().register(this);
        StatusNetwork.getAll(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onCreditsEvent(final Credits credits) {
        creditsTextView.setText(credits.balance + " credits");
    }

    @Subscribe
    public void onRanksEvents(Ranks ranks) {
        // TODO : update ranks
    }

    public void endLoading()
    {
        swipeRefreshLayout.setRefreshing(false);
    }
}
