package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.network.CommunityGoalsNetwork;

public class CommunityGoalsFragment extends Fragment {

    @BindView(R.id.goalsRecyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.emptySwipe)
    public SwipeRefreshLayout emptySwipeRefreshLayout;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community_goals, container, false);
        ButterKnife.bind(this, v);

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new CommunityGoalsAdapter(this));

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        emptySwipeRefreshLayout.setOnRefreshListener(listener);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set visible
        setListVisibility(false);
        EventBus.getDefault().register(this);
        CommunityGoalsNetwork.getCommunityGoals(getView());
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

    public void setListVisibility(boolean visibility)
    {
        if (visibility) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            emptySwipeRefreshLayout.setVisibility(View.GONE);
        }
        else {
            swipeRefreshLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            emptySwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }
}
