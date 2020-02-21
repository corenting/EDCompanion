package fr.corenting.edcompanion.fragments

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import fr.corenting.edcompanion.activities.SystemDetailsActivity
import fr.corenting.edcompanion.adapters.SystemStationsAdapter
import fr.corenting.edcompanion.models.events.SystemStations
import fr.corenting.edcompanion.utils.NotificationsUtils
import kotlinx.android.synthetic.main.fragment_list.*

class SystemStationsFragment : AbstractListFragment<SystemStationsAdapter>() {
    init {
        loadDataOnCreate = false
    }

    override fun getNewRecyclerViewAdapter(): SystemStationsAdapter {
        return SystemStationsAdapter(context, recyclerView)
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

    companion object {
        const val SYSTEM_STATIONS_FRAGMENT_TAG = "system_stations"
    }
}
