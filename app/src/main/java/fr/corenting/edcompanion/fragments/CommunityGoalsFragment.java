package fr.corenting.edcompanion.fragments;

import android.content.Context;

import org.greenrobot.eventbus.Subscribe;

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
