package fr.corenting.edcompanion.fragments

import androidx.fragment.app.activityViewModels
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter
import fr.corenting.edcompanion.models.CommunityGoal
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.events.CommunityGoals
import fr.corenting.edcompanion.models.events.DistanceSearch
import fr.corenting.edcompanion.network.CommunityGoalsNetwork
import fr.corenting.edcompanion.network.DistanceCalculatorNetwork
import fr.corenting.edcompanion.utils.CommanderUtils
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.view_models.CommanderViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class CommunityGoalsFragment : AbstractListFragment<CommunityGoalsAdapter>() {

    private var playerSystemName: String? = null
    private var communityGoals: List<CommunityGoal> = ArrayList()

    private val viewModel: CommanderViewModel by activityViewModels()

    override fun getNewRecyclerViewAdapter(): CommunityGoalsAdapter {
        return CommunityGoalsAdapter(context, binding.recyclerView, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCommunityGoalEvent(goals: CommunityGoals) {
        // Error
        if (!goals.success) {
            endLoading(true)
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
            return
        }

        endLoading(goals.goalsList.isEmpty())
        communityGoals = goals.goalsList
        recyclerViewAdapter.submitList(communityGoals)


        val currentContext = context
        if (currentContext != null && CommanderUtils.hasPositionData(currentContext)) {
            viewModel.getPosition().observe(viewLifecycleOwner) { result ->
                if (result?.data == null || result.error != null) {
                    NotificationsUtils.displaySnackbar(
                        activity,
                        getString(R.string.cg_player_position_error)
                    )
                } else {
                    refreshDisplayWithCommanderPosition(result.data)
                }
            }
            viewModel.fetchPosition()
        }
    }

    private fun refreshDisplayWithCommanderPosition(position: CommanderPosition) {
        playerSystemName = position.systemName

        val distancesToCompute = ArrayList<String>()

        // Get each system to compute distance for (unique)
        for (goal in communityGoals) {
            if (!distancesToCompute.contains(goal.system)) {
                distancesToCompute.add(goal.system)
            }
        }

        // Send distance requests
        for (system in distancesToCompute) {
            DistanceCalculatorNetwork.getDistance(context, position.systemName, system)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDistanceEvent(distanceSearch: DistanceSearch) {
        val currentContext = context
        if (currentContext != null && CommanderUtils.hasPositionData(currentContext) &&
            !distanceSearch.success && playerSystemName != null
        ) {
            NotificationsUtils.displaySnackbar(
                activity,
                getString(R.string.cg_player_distance_error)
            )
            return
        }

        // Copy list, edit matching item to add distance
        for (i in communityGoals.indices) {
            val communityGoal = communityGoals[i]

            if (communityGoal.system == distanceSearch.endSystemName && playerSystemName == distanceSearch.startSystemName) {

                communityGoal.distanceToPlayer = distanceSearch.distance
            }
        }

        recyclerViewAdapter.submitList(null)
        recyclerViewAdapter.submitList(communityGoals) // submit a copy for diffutils to work
    }

    override fun getData() {
        CommunityGoalsNetwork.getCommunityGoals(context)
    }

    companion object {

        const val COMMUNITY_GOALS_FRAGMENT_TAG = "community_goals_fragment"
    }
}
