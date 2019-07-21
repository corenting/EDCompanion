package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.SystemDetailsActivity;
import fr.corenting.edcompanion.models.CommodityDetailsResult;
import fr.corenting.edcompanion.utils.MathUtils;

public class CommodityDetailsFragment extends Fragment {

    public static final String COMMODITY_DETAILS_FRAGMENT = "commodity_details_fragment";

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.commodityNameTextView)
    public TextView commodityNameTextView;
    @BindView(R.id.isRareTextView)
    public TextView isRareTextView;
    @BindView(R.id.categoryTextView)
    public TextView categoryTextView;
    @BindView(R.id.averageBuyTextView)
    public TextView averageBuyTextView;
    @BindView(R.id.averageSellTextView)
    public TextView averageSellTextView;
    @BindView(R.id.minBuyTextView)
    public TextView minBuyTextView;
    @BindView(R.id.maxSellTextView)
    public TextView maxSellTextView;

    private NumberFormat numberFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_commodity_details, container, false);
        ButterKnife.bind(this, v);

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = () -> {
            swipeRefreshLayout.setRefreshing(true);
            if (getActivity() != null) {
                ((SystemDetailsActivity) getActivity()).getData();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);

        // Init number format
        numberFormat = MathUtils.getNumberFormat(getContext());

        // Setup views
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

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

        // Register event and get the news
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    // Get results posted from parent activity
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityDetailsEvent(CommodityDetailsResult commodityDetailsResult) {
        endLoading();

        bindInformations(commodityDetailsResult);
    }

    private void bindInformations(CommodityDetailsResult details) {
        commodityNameTextView.setText(details.getName());
        isRareTextView.setVisibility(details.isRare() ? View.VISIBLE : View.GONE);
        categoryTextView.setText(details.getCategory().getName());

        averageBuyTextView.setText(getString(R.string.credits,
                numberFormat.format(details.getAverageBuyPrice())));
        averageSellTextView.setText(getString(R.string.credits,
                numberFormat.format(details.getAverageSellPrice())));

        minBuyTextView.setText(getString(R.string.credits,
                numberFormat.format(details.getMinimumBuyPrice())));
        maxSellTextView.setText(getString(R.string.credits,
                numberFormat.format(details.getMaximumSellPrice())));
    }

    public void endLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
