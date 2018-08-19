package fr.corenting.edcompanion.fragments;

import android.util.Log;

import org.greenrobot.eventbus.Subscribe;

import fr.corenting.edcompanion.adapters.ShipFinderAdapter;
import fr.corenting.edcompanion.models.ResultsList;
import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.models.ShipFinderSearchEvent;
import fr.corenting.edcompanion.network.ShipFinderNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class ShipFinderFragment extends FinderFragment<ShipFinderAdapter> {

    public static final String SHIP_FINDER_FRAGMENT_TAG = "ship_finder_fragment";

    @Override
    public ShipFinderAdapter getNewRecyclerViewAdapter() {
        return new ShipFinderAdapter(getContext());
    }

    @Subscribe
    public void onShipFinderResultEvent(ResultsList<ShipFinderResult> results) {
        // Error
        if (!results.Success) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(results.Results == null || results.Results.size() == 0);
        recyclerViewAdapter.setResults(results.Results);
    }

    @Subscribe
    public void onFindButtonEvent(ShipFinderSearchEvent event) {
        startLoading();
        ShipFinderNetwork.findShip(getContext(), event.SystemName, event.ShipName);
    }
}
