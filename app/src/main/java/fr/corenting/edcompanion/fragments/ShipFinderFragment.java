package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.ShipFinderAdapter;
import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.models.ShipFinderResults;
import fr.corenting.edcompanion.network.ShipFinderNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ShipFinderFragment extends Fragment {

    public static final String SHIP_FINDER_FRAGMENT_TAG = "ship_finder_fragment";

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    public MaterialProgressBar progressBar;

    private ShipFinderAdapter shipFinderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ship_finder, container, false);
        ButterKnife.bind(this, v);

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        shipFinderAdapter = new ShipFinderAdapter(getContext(), this);
        recyclerView.setAdapter(shipFinderAdapter);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Register eventbus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void endLoading(boolean isEmpty)
    {
        shipFinderAdapter.getEmptyTextView().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void startLoading()
    {
        shipFinderAdapter.setResults(new LinkedList<ShipFinderResult>());
        progressBar.setVisibility(View.VISIBLE);
        shipFinderAdapter.getEmptyTextView().setVisibility(View.GONE);
    }

    @Subscribe
    public void onShipFinderResultEvent(ShipFinderResults results) {
        // Error
        if (!results.Success) {
            endLoading(true);
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }
        else
        {
            endLoading(results.Results == null || results.Results.size() == 0);
        }
        shipFinderAdapter.setResults(results.Results);
    }

    public void onFindButtonClick(Button button, String systemName, String shipName) {
        ViewUtils.hideSoftKeyboard(button.getRootView());
        startLoading();

        ShipFinderNetwork.findShip(getContext(), systemName, shipName);
    }
}
