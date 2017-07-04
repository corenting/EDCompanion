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
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.network.CommunityGoalsNetwork;

public class CommunityGoalsFragment extends Fragment  {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.emptySwipe)
    public SwipeRefreshLayout emptySwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community_goals, container, false);
        ButterKnife.bind(this, v);

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new CommunityGoalsAdapter(getContext(), recyclerView, false));

        //Swipe to refresh setup
        final CommunityGoalsFragment parent = this;
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CommunityGoalsAdapter adapter = (CommunityGoalsAdapter) recyclerView.getAdapter();
                endLoading(0);
                adapter.clearGoals();
                emptySwipeRefreshLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                CommunityGoalsNetwork.getCommunityGoals(parent);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        emptySwipeRefreshLayout.setOnRefreshListener(listener);


        return v;
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
        CommunityGoalsNetwork.getCommunityGoals(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onCommunityGoalEvent(CommunityGoal goal) {
        CommunityGoalsAdapter adapter = (CommunityGoalsAdapter) recyclerView.getAdapter();
        adapter.addGoal(goal);
    }

    public void endLoading(int count)
    {
        swipeRefreshLayout.setRefreshing(false);
        emptySwipeRefreshLayout.setRefreshing(false);
        if (count <= 0)
        {
            emptySwipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }
}
