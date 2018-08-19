package fr.corenting.edcompanion.fragments;

import android.util.Log;

import org.greenrobot.eventbus.Subscribe;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommodityFinderAdapter;
import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.CommodityFinderSearchEvent;
import fr.corenting.edcompanion.models.ResultsList;
import fr.corenting.edcompanion.network.CommodityFinderNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommodityFinderFragment extends FinderFragment<CommodityFinderAdapter> {

    public static final String COMMODITY_FINDER_FRAGMENT_TAG = "commodity_finder_fragment";

    @Override
    public CommodityFinderAdapter getNewRecyclerViewAdapter() {
        return new CommodityFinderAdapter(getContext());
    }

    @Subscribe
    public void onShipFinderResultEvent(ResultsList<CommodityFinderResult> results) {
        Log.d("Event", "Results called");
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
    public void onFindButtonEvent(CommodityFinderSearchEvent event) {
        startLoading();

        // Don't send any as landing pad
        if (event.LandingPadSize.equals(getResources().getStringArray(R.array.landing_pad_size)[0])) {
            event.LandingPadSize = null;
        }

        CommodityFinderNetwork.findCommodity(getContext(), event.SystemName, event.CommodityName,
                event.LandingPadSize, event.Stock);
    }
}