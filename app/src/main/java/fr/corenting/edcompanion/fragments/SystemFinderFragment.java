package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.SystemFinderAdapter;
import fr.corenting.edcompanion.models.SystemFinderResult;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.models.events.SystemFinderSearch;
import fr.corenting.edcompanion.network.SystemNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class SystemFinderFragment extends AbstractFinderFragment<SystemFinderAdapter> {

    public static final String SYSTEM_FINDER_FRAGMENT_TAG = "system_finder_fragment";

    private SystemFinderSearch lastSearch;

    @Override
    public SystemFinderAdapter getNewRecyclerViewAdapter() {
        return new SystemFinderAdapter(getContext());
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
    public void onShipFinderResultEvent(ResultsList<SystemFinderResult> results) {
        // Error
        if (!results.getSuccess()) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(results.getResults().size() == 0);
        recyclerViewAdapter.setResults(results.getResults());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFindButtonEvent(SystemFinderSearch event) {
        lastSearch = event;
        startLoading();
        SystemNetwork.findSystem(getContext(), event.getSystemName());
    }
}
