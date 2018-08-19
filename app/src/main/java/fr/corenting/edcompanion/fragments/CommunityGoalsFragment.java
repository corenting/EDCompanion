package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;

import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter;
import fr.corenting.edcompanion.models.CommunityGoals;
import fr.corenting.edcompanion.models.ShipFinderSearchEvent;
import fr.corenting.edcompanion.network.CommunityGoalsNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommunityGoalsFragment extends ListFragment<CommunityGoalsAdapter> {

    public static final String COMMUNITY_GOALS_FRAGMENT_TAG = "community_goals_fragment";

    private ShipFinderSearchEvent lastSearch;


    @Subscribe
    public void onCommunityGoalEvent(CommunityGoals goals) {
        // Error
        if (!goals.Success) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(false);
        recyclerViewAdapter.add(goals.GoalsList);
    }

    @Override
    void getData() {
        CommunityGoalsNetwork.getCommunityGoals(getContext());
    }

    @Override
    CommunityGoalsAdapter getAdapter() {
        return new CommunityGoalsAdapter(getContext(), recyclerView, false);
    }
}
