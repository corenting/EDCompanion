package fr.corenting.edcompanion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.LoginActivity;
import fr.corenting.edcompanion.activities.SettingsActivity;
import fr.corenting.edcompanion.databinding.FragmentCommanderStatusBinding;
import fr.corenting.edcompanion.models.events.CommanderPosition;
import fr.corenting.edcompanion.models.events.Credits;
import fr.corenting.edcompanion.models.events.FrontierAuthNeeded;
import fr.corenting.edcompanion.models.events.Ranks;
import fr.corenting.edcompanion.network.player.PlayerNetwork;
import fr.corenting.edcompanion.utils.InternalNamingUtils;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.utils.MiscUtils;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.OAuthUtils;
import fr.corenting.edcompanion.utils.PlayerNetworkUtils;
import fr.corenting.edcompanion.utils.RankUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;

public class CommanderStatusFragment extends Fragment {

    public static final String COMMANDER_STATUS_FRAGMENT = "commander_status_fragment";

    private FragmentCommanderStatusBinding binding;
    private NumberFormat numberFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommanderStatusBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // Number format
        numberFormat = MathUtils.getNumberFormat(getContext());

        binding.locationContainer.setOnClickListener(this::onClick);

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = () -> {
            binding.swipeContainer.setRefreshing(true);
            getAll();
        };
        binding.swipeContainer.setOnRefreshListener(listener);

        // Set temporary text
        binding.creditsTextView.setText(getResources().getString(R.string.credits, "?"));
        binding.locationTextView.setText(getResources().getString(R.string.unknown));
        RankUtils.setTempContent(getContext(), binding.federationRankLayout.getRoot(),
                getString(R.string.rank_federation));
        RankUtils.setTempContent(getContext(), binding.empireRankLayout.getRoot(), getString(R.string.rank_empire));

        RankUtils.setTempContent(getContext(), binding.combatRankLayout.getRoot(), getString(R.string.rank_combat));
        RankUtils.setTempContent(getContext(), binding.tradeRankLayout.getRoot(), getString(R.string.rank_trading));
        RankUtils.setTempContent(getContext(), binding.explorationRankLayout.getRoot(),
                getString(R.string.rank_exploration));
        RankUtils.setTempContent(getContext(), binding.arenaRankLayout.getRoot(), getString(R.string.rank_arena));

        // Hide views according to supported informations from source
        PlayerNetwork playerNetwork = PlayerNetworkUtils.getCurrentPlayerNetwork(getContext());
        if (!playerNetwork.supportCredits()) {
            binding.creditsContainer.setVisibility(View.GONE);
        }
        if (!playerNetwork.supportLocation()) {
            binding.locationContainer.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup views
        binding.swipeContainer.setVisibility(View.VISIBLE);
        binding.swipeContainer.setRefreshing(true);

        // Register event and get the news
        EventBus.getDefault().register(this);
        getAll();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreditsEvent(Credits credits) {
        endLoading();
        // Check download error
        if (!credits.getSuccess()) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        // Check error case
        if (credits.getBalance() == -1) {
            binding.creditsTextView.setText(getResources().getString(R.string.unknown));
            return;
        }

        String amount = numberFormat.format(credits.getBalance());
        if (credits.getLoan() != 0) {
            String loan = numberFormat.format(credits.getLoan());
            binding.creditsTextView.setText(getResources().getString(R.string.credits_with_loan,
                    amount, loan));
        } else {
            binding.creditsTextView.setText(getResources().getString(R.string.credits, amount));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginNeededEvent(FrontierAuthNeeded event) {
        endLoading();

        OAuthUtils.storeUpdatedTokens(getContext(), "", "");

        // Show dialog
        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.login_again_dialog_title)
                .setMessage(R.string.login_again_dialog_text)
                .setPositiveButton(android.R.string.ok, (d, which) -> {
                    d.dismiss();
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                })
                .setNegativeButton(android.R.string.cancel,
                        (d, which) -> d.dismiss())
                .create();

        dialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPositionEvent(CommanderPosition position) {
        endLoading();
        // Check download error
        if (!position.getSuccess()) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        binding.locationTextView.setText(position.getSystemName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRanksEvents(Ranks ranks) {
        endLoading();
        // Check download error
        if (!ranks.getSuccess()) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        RankUtils.setContent(getContext(), binding.federationRankLayout.getRoot(), R.drawable.elite_federation,
                ranks.getFederation(), getString(R.string.rank_federation));
        RankUtils.setContent(getContext(), binding.empireRankLayout.getRoot(), R.drawable.elite_empire,
                ranks.getEmpire(), getString(R.string.rank_empire));

        RankUtils.setContent(getContext(), binding.combatRankLayout.getRoot(),
                InternalNamingUtils.getCombatLogoId(ranks.getCombat().getValue()), ranks.getCombat(),
                getString(R.string.rank_combat));
        RankUtils.setContent(getContext(), binding.tradeRankLayout.getRoot(),
                InternalNamingUtils.getTradeLogoId(ranks.getTrade().getValue()), ranks.getTrade(),
                getString(R.string.rank_trading));
        RankUtils.setContent(getContext(), binding.explorationRankLayout.getRoot(),
                InternalNamingUtils.getExplorationLogoId(ranks.getExplore().getValue()),
                ranks.getExplore(), getString(R.string.rank_exploration));
        RankUtils.setContent(getContext(), binding.arenaRankLayout.getRoot(),
                InternalNamingUtils.getCqcLogoId(ranks.getCqc().getValue()), ranks.getCqc(),
                getString(R.string.rank_arena));
    }

    public void onSystemNameClick() {
        final String text = binding.locationTextView.getText().toString();
        MiscUtils.startIntentToSystemDetails(getContext(), text);
    }

    private void getAll() {
        // Refresh player name card title
        String cmdrName = SettingsUtils.getCommanderName(this.getContext());
        if (cmdrName.length() != 0) {
            binding.commanderNameTextView.setText(cmdrName);
        }

        // Refresh player network object if settings changed
        PlayerNetwork playerNetwork = PlayerNetworkUtils.getCurrentPlayerNetwork(getContext());

        if (PlayerNetworkUtils.setupOk(getContext())) {
            playerNetwork.getCommanderStatus();
        } else {
            NotificationsUtils.displaySnackbarWithActivityButton(getActivity(),
                    playerNetwork.getErrorMessage(), SettingsActivity.class);
            endLoading();
        }
    }

    public void endLoading() {
        binding.swipeContainer.setRefreshing(false);
    }

    private void onClick(View v) {
        onSystemNameClick();
    }
}
