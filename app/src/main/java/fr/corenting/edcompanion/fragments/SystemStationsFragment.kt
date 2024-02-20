package fr.corenting.edcompanion.fragments

import fr.corenting.edcompanion.activities.SystemDetailsActivity
import fr.corenting.edcompanion.adapters.SystemStationsAdapter
import fr.corenting.edcompanion.models.events.SystemStations
import fr.corenting.edcompanion.utils.NotificationsUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SystemStationsFragment : AbstractListFragment<SystemStationsAdapter>() {
    init {
        loadDataOnCreate = false
    }

    override fun getNewRecyclerViewAdapter(): SystemStationsAdapter {
        return SystemStationsAdapter(context, binding.recyclerView)
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
        val parent = activity as SystemDetailsActivity
        parent.getData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStationsEvent(stations: SystemStations) {
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
