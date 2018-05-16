package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommodityFinderAdapter;
import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.CommodityFinderResults;
import fr.corenting.edcompanion.network.CommodityFinderNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class CommodityFinderFragment extends Fragment {

    public static final String COMMODITY_FINDER_FRAGMENT_TAG = "commodity_finder_fragment";

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    public MaterialProgressBar progressBar;

    private CommodityFinderAdapter commodityFinderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finder, container, false);
        ButterKnife.bind(this, v);

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        commodityFinderAdapter = new CommodityFinderAdapter(getContext(), this);
        recyclerView.setAdapter(commodityFinderAdapter);

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
        commodityFinderAdapter.getEmptyTextView().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void startLoading()
    {
        commodityFinderAdapter.setResults(new LinkedList<CommodityFinderResult>());
        progressBar.setVisibility(View.VISIBLE);
        commodityFinderAdapter.getEmptyTextView().setVisibility(View.GONE);
    }

    @Subscribe
    public void onShipFinderResultEvent(CommodityFinderResults results) {
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
        commodityFinderAdapter.setResults(results.Results);
    }

    public void onFindButtonClick(Button button, String system, String commodity,
                                  String landingPad, int minStock) {
        ViewUtils.hideSoftKeyboard(button.getRootView());
        startLoading();

        // Don't send any as landing pad
        if (landingPad.equals(getResources().getStringArray(R.array.landing_pad_size)[0]))
        {
            landingPad = null;
        }

        CommodityFinderNetwork.findCommodity(getContext(), system, commodity, landingPad, minStock);
    }
}
