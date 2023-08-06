package fr.corenting.edcompanion.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.activities.LoginActivity
import fr.corenting.edcompanion.activities.SettingsActivity
import fr.corenting.edcompanion.databinding.FragmentCommanderStatusBinding
import fr.corenting.edcompanion.models.CommanderCredits
import fr.corenting.edcompanion.models.CommanderFleet
import fr.corenting.edcompanion.models.CommanderLoadout
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommanderRanks
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.exceptions.DataNotInitializedException
import fr.corenting.edcompanion.models.exceptions.FrontierAuthNeededException
import fr.corenting.edcompanion.utils.CommanderUtils
import fr.corenting.edcompanion.utils.InternalNamingUtils
import fr.corenting.edcompanion.utils.MathUtils
import fr.corenting.edcompanion.utils.MiscUtils
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.utils.RankUtils
import fr.corenting.edcompanion.view_models.CommanderViewModel

class CommanderStatusFragment : Fragment() {

    companion object {
        const val COMMANDER_STATUS_FRAGMENT = "commander_status_fragment"
    }

    private var frontierLoginNeeded: Boolean = false

    private var _binding: FragmentCommanderStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommanderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommanderStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Click to system details on location name
        binding.locationContainer.setOnClickListener { onSystemNameClick() }

        //Swipe to refresh setup
        val listener = OnRefreshListener {
            binding.swipeContainer.isRefreshing = true
            this.refreshCommanderStatus()
        }
        binding.swipeContainer.setOnRefreshListener(listener)

        // Set temporary text
        binding.creditsTextView.text = resources.getString(R.string.credits, "?")
        binding.locationTextView.text = resources.getString(R.string.unknown)
        RankUtils.setTempContent(
            context, binding.federationRankLayout.root,
            getString(R.string.rank_federation)
        )
        RankUtils.setTempContent(
            context,
            binding.empireRankLayout.root,
            getString(R.string.rank_empire)
        )
        RankUtils.setTempContent(
            context,
            binding.combatRankLayout.root,
            getString(R.string.rank_combat)
        )
        RankUtils.setTempContent(
            context,
            binding.tradeRankLayout.root,
            getString(R.string.rank_trading)
        )
        RankUtils.setTempContent(
            context, binding.explorationRankLayout.root,
            getString(R.string.rank_exploration)
        )
        RankUtils.setTempContent(
            context,
            binding.arenaRankLayout.root,
            getString(R.string.rank_arena)
        )
        RankUtils.setTempContent(
            context,
            binding.exobiologistRankLayout.root,
            getString(R.string.rank_exobiologist)
        )
        RankUtils.setTempContent(
            context,
            binding.mercenaryRankLayout.root,
            getString(R.string.rank_mercenary)
        )

        // Hide views according to supported content from source
        val currentContext = context
        if (currentContext != null) {
            if (!CommanderUtils.hasCreditsData(currentContext)) {
                binding.creditsContainer.visibility = View.GONE
            } else {
                binding.creditsContainer.visibility = View.VISIBLE
            }

            if (!CommanderUtils.hasPositionData(currentContext)) {
                binding.locationContainer.visibility = View.GONE
            } else {
                binding.locationContainer.visibility = View.VISIBLE
            }

            if (!CommanderUtils.hasOdysseyRanks(currentContext)) {
                binding.exobiologistRankLayout.rankRelativeLayout.visibility = View.GONE
                binding.mercenaryRankLayout.rankRelativeLayout.visibility = View.GONE
            } else {
                binding.exobiologistRankLayout.rankRelativeLayout.visibility = View.VISIBLE
                binding.mercenaryRankLayout.rankRelativeLayout.visibility = View.VISIBLE
            }
        }

        // Setup observers
        viewModel.getRanks().observe(viewLifecycleOwner) {
            onRanksChange(it)
        }
        viewModel.getCredits().observe(viewLifecycleOwner) {
            onCreditsChange(it)
        }
        viewModel.getPosition().observe(viewLifecycleOwner) {
            onPositionChange(it)
        }
        viewModel.getFleet().observe(viewLifecycleOwner) {
            onFleetChange(it)
        }
        viewModel.getLoadout().observe(viewLifecycleOwner) {
            onLoadoutChange(it)
        }

        // Display message if no source, else fetch content
        if (currentContext != null) {
            if (CommanderUtils.hasCommanderStatus(currentContext)) {
                startLoading()
                this.refreshCommanderStatus()
            } else {

                val dialog = MaterialAlertDialogBuilder(currentContext)
                    .setTitle(R.string.error)
                    .setMessage(R.string.no_commander_accounts_linked_error_message)
                    .setPositiveButton(R.string.no_commander_accounts_linked_error_settings_button) { d: DialogInterface, _: Int ->
                        d.dismiss()
                        val i = Intent(context, SettingsActivity::class.java)
                        startActivity(i)
                    }
                    .setNegativeButton(
                        android.R.string.cancel
                    ) { d: DialogInterface, _: Int -> d.dismiss() }
                    .create()
                dialog.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFrontierLoginNeeded() {
        // End loading and clear the data on the viewmodel has it's not valid without login
        viewModel.clearCachedData()
        endLoading()

        // Show dialog but only once
        synchronized(frontierLoginNeeded) {
            if (frontierLoginNeeded) {
                return
            }

            frontierLoginNeeded = true
            val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.login_again_dialog_title)
                .setMessage(R.string.login_again_dialog_text)
                .setPositiveButton(android.R.string.ok) { d: DialogInterface, _: Int ->
                    d.dismiss()
                    val i = Intent(context, LoginActivity::class.java)
                    startActivity(i)
                }
                .setNegativeButton(
                    android.R.string.cancel
                ) { d: DialogInterface, _: Int -> d.dismiss() }
                .setOnDismissListener {
                    it.dismiss()
                    frontierLoginNeeded = false
                }

            val myDialog = dialogBuilder.create()
            myDialog.show()
        }
    }

    private fun onSystemNameClick() {
        val text = binding.locationTextView.text.toString()
        MiscUtils.startIntentToSystemDetails(context, text)
    }

    private fun refreshCommanderStatus() {
        val currentContext = context
        if (currentContext != null) {
            val cmdrName = CommanderUtils.getCommanderName(currentContext)
            if (cmdrName.isNotEmpty()) {
                binding.commanderNameTextView.text = cmdrName
            }
        }

        viewModel.fetchCredits()
        viewModel.fetchPosition()
        viewModel.fetchRanks()
        viewModel.fetchLoadout()

        // We do not really need fleet except to preload for other tab and to display auth popup if needed
        viewModel.fetchFleet()
    }

    private fun <T> handleResult(result: ProxyResult<T>, onSuccess: (ProxyResult<T>) -> Unit) {
        endLoading()

        if (result.error is FrontierAuthNeededException) {
            onFrontierLoginNeeded()
            return
        }

        if (result.data == null || result.error != null) {
            if (result.error !is DataNotInitializedException) {
                NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
            }
        } else {
            onSuccess(result)
        }
    }

    private fun onFleetChange(result: ProxyResult<CommanderFleet>) {
        handleResult(result) {}
    }

    private fun onLoadoutChange(result: ProxyResult<CommanderLoadout>) {
        handleResult(result) {
            if (result.data == null) {
                return@handleResult
            }

            if (result.data.hasLoadout) {
                binding.loadoutContainer.visibility = View.VISIBLE
                binding.suitTextView.text = result.data.suitName
            } else {
                binding.loadoutContainer.visibility = View.GONE
            }
        }
    }


    private fun onPositionChange(result: ProxyResult<CommanderPosition>) {
        handleResult(result) {
            if (result.data == null) {
                return@handleResult
            }
            binding.locationTextView.text = result.data.systemName
        }
    }

    private fun onCreditsChange(result: ProxyResult<CommanderCredits>) {
        handleResult(result) {
            if (result.data == null) {
                return@handleResult
            }

            val numberFormat = MathUtils.getNumberFormat(context)
            val amount: String = numberFormat.format(result.data.balance)
            if (result.data.loan > 0) {
                val loan: String = numberFormat.format(result.data.loan)
                binding.creditsTextView.text = resources.getString(
                    R.string.credits_with_loan,
                    amount, loan
                )
            } else {
                binding.creditsTextView.text = resources.getString(R.string.credits, amount)
            }
        }
    }

    private fun onRanksChange(result: ProxyResult<CommanderRanks>) {
        handleResult(result) {
            if (result.data == null) {
                return@handleResult
            }

            val ranks = result.data
            RankUtils.setContent(
                context, binding.federationRankLayout.root, R.drawable.elite_federation,
                ranks.federation, getString(R.string.rank_federation)
            )
            RankUtils.setContent(
                context, binding.empireRankLayout.root, R.drawable.elite_empire,
                ranks.empire, getString(R.string.rank_empire)
            )
            RankUtils.setContent(
                context, binding.combatRankLayout.root,
                InternalNamingUtils.getCombatLogoId(ranks.combat.value), ranks.combat,
                getString(R.string.rank_combat)
            )
            RankUtils.setContent(
                context, binding.tradeRankLayout.root,
                InternalNamingUtils.getTradeLogoId(ranks.trade.value), ranks.trade,
                getString(R.string.rank_trading)
            )
            RankUtils.setContent(
                context, binding.explorationRankLayout.root,
                InternalNamingUtils.getExplorationLogoId(ranks.explore.value),
                ranks.explore, getString(R.string.rank_exploration)
            )
            RankUtils.setContent(
                context, binding.arenaRankLayout.root,
                InternalNamingUtils.getCqcLogoId(ranks.cqc.value), ranks.cqc,
                getString(R.string.rank_arena)
            )

            if (ranks.exobiologist != null) {
                RankUtils.setContent(
                    context, binding.exobiologistRankLayout.root,
                    R.drawable.rank_placeholder, ranks.exobiologist,
                    getString(R.string.rank_exobiologist)
                )
            }
            if (ranks.mercenary != null) {
                RankUtils.setContent(
                    context, binding.mercenaryRankLayout.root,
                    R.drawable.rank_placeholder, ranks.mercenary,
                    getString(R.string.rank_mercenary)
                )
            }
        }
    }

    fun endLoading() {
        binding.swipeContainer.post {
            binding.swipeContainer.isRefreshing = false
        }
    }

    private fun startLoading() {
        binding.swipeContainer.post {
            binding.swipeContainer.isRefreshing = true
        }
    }
}