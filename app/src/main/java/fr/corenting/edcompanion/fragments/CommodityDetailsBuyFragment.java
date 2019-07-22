package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.CommodityDetailsStationsAdapter;
import fr.corenting.edcompanion.models.events.CommodityDetailsBuy;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommodityDetailsBuyFragment extends AbstractListFragment<CommodityDetailsStationsAdapter> {

    public static final String COMMODITY_DETAILS_BUY_FRAGMENT_TAG = "commodity_details_buy";

    public CommodityDetailsBuyFragment() {
        loadDataOnCreate = false;
    }

    @Override
    void getData() {
        // none : data is sent by parent activity
    }

    @Override
    CommodityDetailsStationsAdapter getAdapter() {
        return new CommodityDetailsStationsAdapter(getContext(), false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSellStationsEvents(CommodityDetailsBuy stations) {
        // Error case
        if (!stations.getSuccess()) {
            endLoading(true);
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        endLoading(stations.getStations().size() == 0);
        recyclerViewAdapter.submitList(stations.getStations());
    }
}
