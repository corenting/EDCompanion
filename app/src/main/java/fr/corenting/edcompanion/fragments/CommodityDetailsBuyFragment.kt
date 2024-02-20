package fr.corenting.edcompanion.fragments

import fr.corenting.edcompanion.adapters.CommodityDetailsStationsAdapter
import fr.corenting.edcompanion.models.events.CommodityDetailsBuy
import fr.corenting.edcompanion.utils.NotificationsUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CommodityDetailsBuyFragment : AbstractListFragment<CommodityDetailsStationsAdapter>() {
    init {
        loadDataOnCreate = false
    }

    override fun getNewRecyclerViewAdapter(): CommodityDetailsStationsAdapter {
        return CommodityDetailsStationsAdapter(context, false)
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
    }


    override fun getData() {
        // none : data is sent by parent activity
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSellStationsEvents(stations: CommodityDetailsBuy) {
        // Error case
        if (!stations.success) {
            endLoading(true)
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
            return
        }

        endLoading(stations.stations.isEmpty())
        recyclerViewAdapter.submitList(stations.stations)
    }
}
