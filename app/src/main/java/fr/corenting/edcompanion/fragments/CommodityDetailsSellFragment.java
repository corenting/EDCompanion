package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.CommodityDetailsStationsAdapter;
import fr.corenting.edcompanion.models.events.CommodityDetailsSell;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommodityDetailsSellFragment extends AbstractListFragment<CommodityDetailsStationsAdapter> {

    public static final String COMMODITY_DETAILS_SELL_FRAGMENT_TAG = "commodity_details_sell";

    public CommodityDetailsSellFragment() {
        loadDataOnCreate = false;
    }

    @Override
    void getData() {
        // none : data is sent by parent activity
    }

    @Override
    CommodityDetailsStationsAdapter getAdapter() {
        return new CommodityDetailsStationsAdapter(getContext(), true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSellStationsEvents(CommodityDetailsSell stations) {
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
