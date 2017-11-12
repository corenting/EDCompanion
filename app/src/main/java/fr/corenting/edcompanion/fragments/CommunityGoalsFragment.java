package fr.corenting.edcompanion.fragments;

import android.content.Context;
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

public class CommunityGoalsFragment extends ListFragment {

    public static final String COMMUNITY_GOALS_FRAGMENT_TAG = "community_goals_fragment";

    @Subscribe
    public void onCommunityGoalEvent(CommunityGoals goals) {
        // Error
        if (!goals.Success) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        // Else setup adapter
        endLoading(false);
        CommunityGoalsAdapter adapter = new CommunityGoalsAdapter(getContext(), recyclerView, goals.GoalsList, false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    void getData(Context context) {
        CommunityGoalsNetwork.getCommunityGoals(getContext());
    }
}
