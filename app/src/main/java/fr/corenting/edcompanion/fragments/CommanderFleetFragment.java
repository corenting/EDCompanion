package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.CommanderFleetAdapter;
import fr.corenting.edcompanion.models.events.Fleet;
import fr.corenting.edcompanion.network.CommunityGoalsNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommanderFleetFragment extends AbstractListFragment<CommanderFleetAdapter> {

    public static final String COMMANDER_FLEET_FRAGMENT_TAG = "commander_fleet_fragment";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFleetEvent(Fleet fleet) {
        // Error
        if (!fleet.getSuccess()) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        // Else setup adapter
        endLoading(false);
        recyclerViewAdapter.submitList(fleet.getShips());
    }

    @Override
    void getData() {
        CommunityGoalsNetwork.getCommunityGoals(getContext());
    }

    @Override
    CommanderFleetAdapter getAdapter() {
        return new CommanderFleetAdapter(getContext(), recyclerView);
    }
}
