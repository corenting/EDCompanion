package fr.corenting.edcompanion.fragments

import androidx.fragment.app.activityViewModels
import fr.corenting.edcompanion.adapters.CommanderFleetAdapter
import fr.corenting.edcompanion.adapters.CommanderLoadoutsAdapter
import fr.corenting.edcompanion.models.exceptions.DataNotInitializedException
import fr.corenting.edcompanion.models.exceptions.FrontierAuthNeededException
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.view_models.CommanderViewModel

class CommanderLoadoutsFragment : AbstractListFragment<CommanderLoadoutsAdapter>() {

    private val viewModel: CommanderViewModel by activityViewModels()

    override fun getNewRecyclerViewAdapter(): CommanderLoadoutsAdapter {
        return CommanderLoadoutsAdapter(context)
    }

    override fun getData() {
        viewModel.getAllLoadouts().observe(viewLifecycleOwner) { result ->
            endLoading(false)

            if (result?.data == null || result.error != null) {

                // For frontier auth error there will be a popup displayed by the other fragment anyway
                if (result.error !is FrontierAuthNeededException && result.error !is DataNotInitializedException) {
                    NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
                }
            } else {
                recyclerViewAdapter.submitList(result.data.loadouts)
            }

        }
        viewModel.fetchAllLoadouts()
    }

    override fun needEventBus(): Boolean {
        return false
    }

    companion object {
        const val COMMANDER_LOADOUTS_FRAGMENT_TAG = "commander_loadouts_fragment"
    }
}
