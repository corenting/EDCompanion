package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.activities.SettingsActivity;
import fr.corenting.edcompanion.adapters.CommanderFleetAdapter;
import fr.corenting.edcompanion.models.events.Fleet;
import fr.corenting.edcompanion.network.player.PlayerNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.PlayerNetworkUtils;

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
        // Refresh player network object if settings changed
        PlayerNetwork playerNetwork = PlayerNetworkUtils.getCurrentPlayerNetwork(getContext());

        if (PlayerNetworkUtils.setupOk(getContext())) {
            playerNetwork.getCommanderStatus();
        } else {
            NotificationsUtils.displaySnackbarWithActivityButton(getActivity(),
                    playerNetwork.getErrorMessage(), SettingsActivity.class);
            endLoading(true);
        }
    }

    @Override
    CommanderFleetAdapter getAdapter() {
        return new CommanderFleetAdapter(getContext());
    }
}
