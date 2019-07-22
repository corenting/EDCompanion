package fr.corenting.edcompanion.activities;

import androidx.fragment.app.FragmentPagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import fr.corenting.edcompanion.adapters.CommodityDetailsPagerAdapter;
import fr.corenting.edcompanion.models.events.CommodityDetails;
import fr.corenting.edcompanion.models.events.CommodityDetailsBuy;
import fr.corenting.edcompanion.models.events.CommodityDetailsSell;
import fr.corenting.edcompanion.network.CommoditiesNetwork;

public class CommodityDetailsActivity extends AbstractViewPagerActivity {

    @NotNull
    @Override
    public String getDefaultData() {
        return "Cobalt";
    }

    @NotNull
    @Override
    public FragmentPagerAdapter getPagerAdapter() {
        return new CommodityDetailsPagerAdapter(getSupportFragmentManager(), this);
    }

    @Override
    public void getData() {
        CommoditiesNetwork.getCommodityDetails(this, dataName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityDetailsEvent(CommodityDetails commodityDetailsEvent) {
        if (commodityDetailsEvent.getSuccess() &&
                commodityDetailsEvent.getCommodityDetails() != null) {

            EventBus.getDefault().post(commodityDetailsEvent.getCommodityDetails());
            EventBus.getDefault().post(
                    new CommodityDetailsSell(true,
                            commodityDetailsEvent.getCommodityDetails().getMaximumSellers())
            );
            EventBus.getDefault().post(
                    new CommodityDetailsBuy(true,
                            commodityDetailsEvent.getCommodityDetails().getMinimumBuyers())
            );
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
