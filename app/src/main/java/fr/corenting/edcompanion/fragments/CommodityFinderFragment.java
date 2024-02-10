package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(results.getResults().size() == 0);
        recyclerViewAdapter.setResults(results.getResults());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFindButtonEvent(CommodityFinderSearch event) {
        startLoading();

        lastSearch = event;
        CommodityFinderNetwork.findCommodity(requireContext(), event.getSystemName(),
                event.getCommodityName(), event.getLandingPadSize(), event.getStockOrDemand(),
                event.isSellingMode());
    }
}