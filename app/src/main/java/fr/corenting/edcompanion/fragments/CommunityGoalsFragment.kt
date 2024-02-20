package fr.corenting.edcompanion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommunityGoal
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.SystemsDistance
import fr.corenting.edcompanion.models.events.CommunityGoals
import fr.corenting.edcompanion.utils.CommanderUtils
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.view_models.CommanderViewModel
import fr.corenting.edcompanion.view_models.CommunityGoalsViewModel
import fr.corenting.edcompanion.view_models.DistanceCalculatorViewModel


class CommunityGoalsFragment : AbstractListFragment<CommunityGoalsAdapter>() {

    private var playerSystemName: String? = null
    private var communityGoals: List<CommunityGoal> = ArrayList()

    private val commanderViewModel: CommanderViewModel by activityViewModels()
    private val distanceViewModel: DistanceCalculatorViewModel by activityViewModels()
    private val communityGoalsViewModel: CommunityGoalsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        communityGoalsViewModel.getCommunityGoals()
            .observe(viewLifecycleOwner, ::onCommunityGoalEvent)
        return view;
    }

    override fun getNewRecyclerViewAdapter(): CommunityGoalsAdapter {
        return CommunityGoalsAdapter(context, binding.recyclerView, false)
    }

    private fun onCommunityGoalEvent(goals: ProxyResult<CommunityGoals>) {
        // Error
        if (goals.error != null || goals.data == null) {
            endLoading(true)
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
            return
        }

        endLoading(goals.data.goalsList.isEmpty())
        communityGoals = goals.data.goalsList
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
        distanceViewModel.getDistanceBetweenSystemsResult()
            .observe(viewLifecycleOwner, ::onDistanceResult)
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
        communityGoalsViewModel.fetchCommunityGoals()
    }

    companion object {
        const val COMMUNITY_GOALS_FRAGMENT_TAG = "community_goals_fragment"
    }
}
