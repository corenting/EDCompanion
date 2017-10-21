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

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter;
import fr.corenting.edcompanion.models.CommunityGoals;
import fr.corenting.edcompanion.network.CommunityGoalsNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommunityGoalsFragment extends Fragment {

    public static final String COMMUNITY_GOALS_FRAGMENT_TAG = "community_goals_fragment";

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.emptySwipe)
    public SwipeRefreshLayout emptySwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, v);

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //Swipe to refresh setup
        final CommunityGoalsFragment parent = this;
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                endLoading(0);
                emptySwipeRefreshLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                CommunityGoalsNetwork.getCommunityGoals(getContext());
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        emptySwipeRefreshLayout.setOnRefreshListener(listener);


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

        // Register event and get the goals
        EventBus.getDefault().register(this);
        CommunityGoalsNetwork.getCommunityGoals(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onCommunityGoalEvent(CommunityGoals goals) {
        stopSwipeToRefresh();
        if (!goals.Success) {
            endLoading(0);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }
        endLoading(goals.GoalsList.size());
        CommunityGoalsAdapter adapter = new CommunityGoalsAdapter(getContext(), recyclerView, goals.GoalsList, false);
        recyclerView.setAdapter(adapter);
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
