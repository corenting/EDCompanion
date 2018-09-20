package fr.corenting.edcompanion.fragments;

import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommodityFinderAdapter;
import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.events.CommodityFinderSearch;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.CommodityFinderNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommodityFinderFragment extends AbstractFinderFragment<CommodityFinderAdapter> {

    public static final String COMMODITY_FINDER_FRAGMENT_TAG = "commodity_finder_fragment";

    private CommodityFinderSearch lastSearch;

    @Override
    public void onSwipeToRefresh() {
        if (lastSearch != null) {
            onFindButtonEvent(lastSearch);
        } else {
            endLoading(true);
        }
    }

    @Override
    public CommodityFinderAdapter getNewRecyclerViewAdapter() {
        return new CommodityFinderAdapter(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShipFinderResultEvent(ResultsList<CommodityFinderResult> results) {
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
    public void onFindButtonEvent(CommodityFinderSearch event) {
        startLoading();

        // Don't send any as landing pad
        String landingPadSize = event.getLandingPadSize();

        if (event.getLandingPadSize().equals(
                getResources().getStringArray(R.array.landing_pad_size)[0])) {
            landingPadSize = null;
        }

        lastSearch = event;
        CommodityFinderNetwork.findCommodity(getContext(), event.getSystemName(),
                event.getCommodityName(), landingPadSize, event.getStock());
    }
}