package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.CommoditiesListAdapter;
import fr.corenting.edcompanion.models.CommoditiesListResult;
import fr.corenting.edcompanion.models.events.CommoditiesListSearch;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.CommoditiesNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class CommoditiesListFragment extends AbstractFinderFragment<CommoditiesListAdapter> {

    public static final String COMMODITIES_LIST_FRAGMENT_TAG = "commodities_list_fragment";

    private CommoditiesListSearch lastSearch;

    @Override
    public void onSwipeToRefresh() {
        if (lastSearch != null) {
            onFindButtonEvent(lastSearch);
        } else {
            endLoading(true);
        }
    }

    @Override
    public CommoditiesListAdapter getNewRecyclerViewAdapter() {
        return new CommoditiesListAdapter(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditiesListResult(ResultsList<CommoditiesListResult> results) {
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
    public void onFindButtonEvent(CommoditiesListSearch event) {
        lastSearch = event;
        startLoading();
        CommoditiesNetwork.getCommoditiesPrices(requireContext(), event.getCommodityName());
    }
}