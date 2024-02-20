package fr.corenting.edcompanion.fragments

import androidx.fragment.app.activityViewModels
import fr.corenting.edcompanion.adapters.CommanderFleetAdapter
import fr.corenting.edcompanion.models.exceptions.DataNotInitializedException
import fr.corenting.edcompanion.models.exceptions.FrontierAuthNeededException
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.view_models.CommanderViewModel

class CommanderFleetFragment : AbstractListFragment<CommanderFleetAdapter>() {

    private val viewModel: CommanderViewModel by activityViewModels()

    override fun getNewRecyclerViewAdapter(): CommanderFleetAdapter {
        return CommanderFleetAdapter(context)
    }

    override fun getData() {
        viewModel.getFleet().observe(viewLifecycleOwner) { result ->
            endLoading(false)

            if (result?.data == null || result.error != null) {

                // For frontier auth error there will be a popup displayed by the other fragment anyway
                if (result.error !is FrontierAuthNeededException && result.error !is DataNotInitializedException) {
                    NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
                }
            } else {
                recyclerViewAdapter.submitList(result.data.ships)
            }

        }
        viewModel.fetchFleet()
    }
}
