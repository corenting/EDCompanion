package fr.corenting.edcompanion.fragments;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.adapters.OutfittingFinderAdapter;
import fr.corenting.edcompanion.models.OutfittingFinderResult;
import fr.corenting.edcompanion.models.events.OutfittingFinderSearch;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.OutfittingFinderNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class OutfittingFinderFragment extends AbstractFinderFragment<OutfittingFinderAdapter> {

    public static final String OUTFITTING_FINDER_FRAGMENT_TAG = "outfitting_finder_fragment";

    private OutfittingFinderSearch lastSearch;

    @Override
    public void onSwipeToRefresh() {
        if (lastSearch != null) {
            onFindButtonEvent(lastSearch);
        } else {
            endLoading(true);
        }
    }

    @Override
    public OutfittingFinderAdapter getNewRecyclerViewAdapter() {
        return new OutfittingFinderAdapter(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOutfittingFinderResult(ResultsList<OutfittingFinderResult> results) {
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
    public void onFindButtonEvent(OutfittingFinderSearch event) {
        startLoading();

        lastSearch = event;
        OutfittingFinderNetwork.findOutfitting(requireContext(), event.getSystemName(),
                event.getOutfittingName(), event.getLandingPadSize());
    }
}