package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.ShipFinderAdapter;
import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.models.events.ShipFinderSearch;
import fr.corenting.edcompanion.network.ShipFinderNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class ShipFinderFragment extends AbstractFinderFragment<ShipFinderAdapter> {

    public static final String SHIP_FINDER_FRAGMENT_TAG = "ship_finder_fragment";

    private ShipFinderSearch lastSearch;

    @Override
    public ShipFinderAdapter getNewRecyclerViewAdapter() {
        return new ShipFinderAdapter(getContext());
    }

    @Override
    public void onSwipeToRefresh() {
        if (lastSearch != null) {
            onFindButtonEvent(lastSearch);
        } else {
            endLoading(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShipFinderResultEvent(ResultsList<ShipFinderResult> results) {
        // Error
        if (!results.getSuccess()) {
            endLoading(true);
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(results.getResults().isEmpty());
        recyclerViewAdapter.setResults(results.getResults());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFindButtonEvent(ShipFinderSearch event) {
        lastSearch = event;
        startLoading();
        ShipFinderNetwork.findShip(requireContext(), event.getSystemName(), event.getShipName());
    }
}
