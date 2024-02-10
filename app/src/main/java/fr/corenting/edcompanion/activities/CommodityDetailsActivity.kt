package fr.corenting.edcompanion.activities

import fr.corenting.edcompanion.adapters.CommodityDetailsPagerAdapter
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.models.events.CommodityBestPrices
import fr.corenting.edcompanion.models.events.CommodityDetails
import fr.corenting.edcompanion.models.events.CommodityDetailsBuy
import fr.corenting.edcompanion.models.events.CommodityDetailsSell
import fr.corenting.edcompanion.network.CommoditiesNetwork
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CommodityDetailsActivity : AbstractViewPagerActivity() {

    override fun getDefaultData(): String {
        return "Cobalt"
    }

    override fun getAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter {
        return CommodityDetailsPagerAdapter(fragmentActivity)
    }

    override fun getTabLayoutMediator(
        context: Context,
        tabLayout: TabLayout,
        viewPager: ViewPager2
    ): TabLayoutMediator {
        return TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text =  when (position) {
                1 -> context.getString(R.string.where_to_sell)
                2 -> context.getString(R.string.where_to_buy)
                else ->  context.getString(R.string.commodity)
            }
        }
    }

    override fun getData() {
        CommoditiesNetwork.getCommodityDetails(this, dataName)
        CommoditiesNetwork.getCommoditiesBestPrices(this, dataName)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCommodityDetailsEvent(commodityDetailsEvent: CommodityDetails) {
        if (commodityDetailsEvent.success && commodityDetailsEvent.commodityDetails != null) {

            EventBus.getDefault().post(commodityDetailsEvent.commodityDetails)

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCommodityBestPricesEvent(commodityBestPricesEvent: CommodityBestPrices) {
        EventBus.getDefault().post(
            CommodityDetailsSell(
                true,
                commodityBestPricesEvent.bestStationsToSell
            )
        )
        EventBus.getDefault().post(
            CommodityDetailsBuy(
                true,
                commodityBestPricesEvent.bestStationsToBuy
            )
        )
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}
