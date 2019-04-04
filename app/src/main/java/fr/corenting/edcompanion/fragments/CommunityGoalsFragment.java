package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.models.events.CommanderPosition;
import fr.corenting.edcompanion.models.events.CommunityGoals;
import fr.corenting.edcompanion.models.events.DistanceSearch;
import fr.corenting.edcompanion.network.CommunityGoalsNetwork;
import fr.corenting.edcompanion.network.DistanceCalculatorNetwork;
import fr.corenting.edcompanion.network.player.PlayerNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.PlayerNetworkUtils;

public class CommunityGoalsFragment extends AbstractListFragment<CommunityGoalsAdapter> {

    public static final String COMMUNITY_GOALS_FRAGMENT_TAG = "community_goals_fragment";

    private String playerSystemName;
    private List<CommunityGoal> communityGoals = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommunityGoalEvent(CommunityGoals goals) {
        // Error
        if (!goals.getSuccess()) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(goals.getGoalsList().isEmpty());
        communityGoals = goals.getGoalsList();
        recyclerViewAdapter.submitList(communityGoals);

        // Then get distance to player
        PlayerNetwork playerNetwork = PlayerNetworkUtils.getCurrentPlayerNetwork(getContext());
        if (PlayerNetworkUtils.setupOk(getContext()) && playerNetwork.supportLocation()) {
            playerNetwork.getCommanderPosition();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPositionEvent(CommanderPosition position) {
        // Check download error
        if (!position.getSuccess()) {
            NotificationsUtils.displaySnackbar(getActivity(),
                    getString(R.string.cg_player_position_error));
            return;
        }
        playerSystemName = position.getSystemName();

        List<String> distancesToCompute = new ArrayList<>();

        // Get each system to compute distance for (unique)
        for (CommunityGoal communityGoal : communityGoals) {
            if (!distancesToCompute.contains(communityGoal.getSystem())) {
                distancesToCompute.add(communityGoal.getSystem());
            }
        }

        // Send distance requests
        for (String system : distancesToCompute) {
            DistanceCalculatorNetwork.getDistance(getContext(), position.getSystemName(), system);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDistanceEvent(DistanceSearch distanceSearch) {
        // Error
        if (PlayerNetworkUtils.setupOk(getContext()) &&
                !distanceSearch.getSuccess() && playerSystemName != null) {
            NotificationsUtils.displaySnackbar(getActivity(),
                    getString(R.string.cg_player_distance_error));
            return;
        }

        // Copy list, edit matching item to add distance
        for (int i = 0; i < communityGoals.size(); i++) {
            CommunityGoal communityGoal = communityGoals.get(i);

            if (communityGoal.getSystem().equals(distanceSearch.getEndSystemName()) &&
                    playerSystemName.equals(distanceSearch.getStartSystemName())) {

                communityGoal.setDistanceToPlayer(distanceSearch.getDistance());
            }
        }

        recyclerViewAdapter.submitList(null);
        recyclerViewAdapter.submitList(communityGoals); // submit a copy for diffutils to work
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
