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
import fr.corenting.edcompanion.models.CommanderPosition
import fr.corenting.edcompanion.models.CommanderRanks
import fr.corenting.edcompanion.models.ProxyResult
import fr.corenting.edcompanion.models.exceptions.FrontierAuthNeededException
import fr.corenting.edcompanion.utils.*
import fr.corenting.edcompanion.view_models.CommanderViewModel

class CommanderStatusFragment : Fragment() {

    companion object {
        const val COMMANDER_STATUS_FRAGMENT = "commander_status_fragment"
    }

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
            this.refreshCommanderInformations()
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


        // Hide views according to supported informations from source
        val currentContext = context
        if (currentContext != null) {
            if (!CommanderUtils.hasCreditsData(currentContext)) {
                binding.creditsContainer.visibility = View.GONE
            }
            if (!CommanderUtils.hasPositionData(currentContext)) {
                binding.locationContainer.visibility = View.GONE
            }
        }

        // Display message if no source, else fetch informations
        if (currentContext != null) {
            if (CommanderUtils.hasCommanderInformations(currentContext)) {
                startLoading()
                this.refreshCommanderInformations()
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
        endLoading()
        OAuthUtils.storeUpdatedTokens(context, "", "")

        // Show dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
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
            .create()
        dialog.show()
    }

    private fun onSystemNameClick() {
        val text = binding.locationTextView.text.toString()
        MiscUtils.startIntentToSystemDetails(context, text)
    }

    private fun refreshCommanderInformations() {
        val currentContext = context
        if (currentContext != null) {
            val cmdrName = CommanderUtils.getCommanderName(currentContext)
            if (!cmdrName.isNullOrEmpty()) {
                binding.commanderNameTextView.text = cmdrName
            }
        }

        viewModel.getRanks().observe(viewLifecycleOwner) {
            onRanksChange(it)
        }
        viewModel.getCredits().observe(viewLifecycleOwner) {
            onCreditsChange(it)
        }
        viewModel.getPosition().observe(viewLifecycleOwner) {
            onPositionChange(it)
        }

        viewModel.fetchCredits()
        viewModel.fetchPosition()
        viewModel.fetchRanks()
    }

    private fun onPositionChange(result: ProxyResult<CommanderPosition>?) {
        endLoading()

        if (result?.error is FrontierAuthNeededException) {
            onFrontierLoginNeeded()
        }

        if (result?.data == null || result.error != null) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
        } else {
            binding.locationTextView.text = result.data.systemName
        }
    }

    private fun onCreditsChange(result: ProxyResult<CommanderCredits>?) {
        endLoading()

        if (result?.error is FrontierAuthNeededException) {
            onFrontierLoginNeeded()
        }

        if (result?.data == null || result.error != null || result.data.balance < 0) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
        } else {
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

    private fun onRanksChange(result: ProxyResult<CommanderRanks>?) {
        endLoading()

        if (result?.error is FrontierAuthNeededException) {
            onFrontierLoginNeeded()
        }

        if (result?.data == null || result.error != null) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(activity)
        } else {
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