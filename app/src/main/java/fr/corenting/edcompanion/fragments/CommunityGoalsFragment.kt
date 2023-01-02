package fr.corenting.edcompanion.fragments

import androidx.fragment.app.activityViewModels
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommunityGoal
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.SystemsDistance
import fr.corenting.edcompanion.models.events.CommunityGoals
import fr.corenting.edcompanion.network.CommunityGoalsNetwork
import fr.corenting.edcompanion.utils.CommanderUtils
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.view_models.CommanderViewModel
import fr.corenting.edcompanion.view_models.DistanceCalculatorViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CommunityGoalsFragment : AbstractListFragment<CommunityGoalsAdapter>() {

    private var playerSystemName: String? = null
    private var communityGoals: List<CommunityGoal> = ArrayList()

    private val commanderViewModel: CommanderViewModel by activityViewModels()
    private val distanceViewModel: DistanceCalculatorViewModel by activityViewModels()

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
            commanderViewModel.getPosition().observe(viewLifecycleOwner) { result ->
                if (result?.data != null && result.error == null) {
                    refreshDisplayWithCommanderPosition(result.data)
                }
            }
            commanderViewModel.fetchPosition()
        }

        // Setup player distance viewmodel observer
        distanceViewModel.getDistanceBetweenSystemsResult().observe(viewLifecycleOwner, {
            onDistanceResult(it)
        })
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
            distanceViewModel.computeDistanceBetweenSystems(
                position.systemName, system
            )
        }
    }

    private fun onDistanceResult(distanceResult: ProxyResult<SystemsDistance>) {
        // Error
        if (distanceResult.data == null || distanceResult.error != null) {
            return
        }

        // Copy list, edit matching item to add distance
        for (i in communityGoals.indices) {
            val communityGoal = communityGoals[i]

            if (communityGoal.system == distanceResult.data.secondSystemName && playerSystemName == distanceResult.data.firstSystemName) {
                communityGoal.distanceToPlayer = distanceResult.data.distanceInLy
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
