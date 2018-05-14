package fr.corenting.edcompanion.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;

import fr.corenting.edcompanion.adapters.ShipFinderAdapter;
import fr.corenting.edcompanion.models.ShipFinderResults;
import fr.corenting.edcompanion.network.CommunityGoalsNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ViewUtils;

public class ShipFinderFragment extends ListFragment {

    public static final String SHIP_FINDER_FRAGMENT_TAG = "ship_finder_fragment";

    private ShipFinderAdapter shipFinderAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        endLoading(false);
        // TODO : first bug with loading shown

        shipFinderAdapter = new ShipFinderAdapter(getContext(), this);
        recyclerView.setAdapter(shipFinderAdapter);
    }

    @Subscribe
    public void onShipFinderResultEvent(ShipFinderResults results) {
        // Error
        if (!results.Success) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        // Else setup adapter
        endLoading(false);
        shipFinderAdapter.setResults(results.Results);
    }

    @Override
    void getData(Context context) {
        CommunityGoalsNetwork.getCommunityGoals(getContext());
    }

    public void onFindButtonClick(Button button) {
        ViewUtils.hideSoftKeyboard(button.getRootView());
        startLoading();

        // TODO : network call
    }
}
