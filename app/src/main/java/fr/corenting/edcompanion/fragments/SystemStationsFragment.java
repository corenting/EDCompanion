package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.activities.SystemDetailsActivity;
import fr.corenting.edcompanion.adapters.SystemStationsAdapter;
import fr.corenting.edcompanion.models.events.SystemStations;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class SystemStationsFragment extends AbstractListFragment<SystemStationsAdapter> {

    public static final String SYSTEM_STATIONS_FRAGMENT_TAG = "system_stations";

    public SystemStationsFragment() {
        loadDataOnCreate = false;
    }

    @Override
    void getData() {
        SystemDetailsActivity parent = ((SystemDetailsActivity) getActivity());
        parent.getData();
    }

    @Override
    SystemStationsAdapter getAdapter() {
        return new SystemStationsAdapter(getContext(), recyclerView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStationsEvent(SystemStations stations) {
        // Error case
        if (!stations.getSuccess()) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(stations.getStations().size() == 0);
        recyclerViewAdapter.submitList(stations.getStations());
    }
}
