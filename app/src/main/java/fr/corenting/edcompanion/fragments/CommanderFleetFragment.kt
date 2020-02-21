package fr.corenting.edcompanion.fragments

import fr.corenting.edcompanion.activities.SettingsActivity
import fr.corenting.edcompanion.adapters.CommanderFleetAdapter
import fr.corenting.edcompanion.models.events.Fleet
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.utils.PlayerNetworkUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CommanderFleetFragment : AbstractListFragment<CommanderFleetAdapter>() {

    override fun getNewRecyclerViewAdapter(): CommanderFleetAdapter {
        return CommanderFleetAdapter(context)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFleetEvent(fleet: Fleet) {
        // Error
        if (!fleet.success) {
            endLoading(true)
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
            return
        }

        // Else setup adapter
        endLoading(false)
        recyclerViewAdapter.submitList(fleet.ships)
    }

    override fun getData() {
        // Refresh player network object if settings changed
        val playerNetwork = PlayerNetworkUtils.getCurrentPlayerNetwork(context)

        if (PlayerNetworkUtils.setupOk(context)) {
            playerNetwork.getCommanderStatus()
        } else {
            NotificationsUtils.displaySnackbarWithActivityButton(activity,
                    playerNetwork.errorMessage, SettingsActivity::class.java)
            endLoading(true)
        }
    }

    companion object {
        const val COMMANDER_FLEET_FRAGMENT_TAG = "commander_fleet_fragment"
    }
}
