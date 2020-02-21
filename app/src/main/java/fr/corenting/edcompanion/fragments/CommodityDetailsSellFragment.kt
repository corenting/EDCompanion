package fr.corenting.edcompanion.fragments

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import fr.corenting.edcompanion.adapters.CommodityDetailsStationsAdapter
import fr.corenting.edcompanion.models.events.CommodityDetailsSell
import fr.corenting.edcompanion.utils.NotificationsUtils

class CommodityDetailsSellFragment : AbstractListFragment<CommodityDetailsStationsAdapter>() {

    init {
        loadDataOnCreate = false
    }

    override fun getNewRecyclerViewAdapter(): CommodityDetailsStationsAdapter {
        return CommodityDetailsStationsAdapter(context, true)
    }

    override fun getData() {
        // none : data is sent by parent activity
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSellStationsEvents(stations: CommodityDetailsSell) {
        // Error case
        if (!stations.success) {
            endLoading(true)
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
            return
        }

        endLoading(stations.stations.isEmpty())
        recyclerViewAdapter.submitList(stations.stations)
    }

    companion object {

        const val COMMODITY_DETAILS_SELL_FRAGMENT_TAG = "commodity_details_sell"
    }
}
