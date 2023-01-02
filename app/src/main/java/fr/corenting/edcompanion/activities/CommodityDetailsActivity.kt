package fr.corenting.edcompanion.activities

import androidx.fragment.app.FragmentPagerAdapter
import fr.corenting.edcompanion.adapters.CommodityDetailsPagerAdapter
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

    override fun getPagerAdapter(): FragmentPagerAdapter {
        return CommodityDetailsPagerAdapter(supportFragmentManager, this)
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
