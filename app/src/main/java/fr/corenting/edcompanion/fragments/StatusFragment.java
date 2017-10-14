package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommanderPosition;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.models.Ranks;
import fr.corenting.edcompanion.network.PlayerStatusNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.RankViewUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;

import static android.R.attr.fragment;

public class StatusFragment extends Fragment {

    public static final String STATUS_FRAGMENT_TAG = "status_fragment";

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.commanderNameTextView)
    public TextView commanderNameTextView;

    @BindView(R.id.creditsTextView)
    public TextView creditsTextView;
    @BindView(R.id.locationTextView)
    public TextView locationsTextView;

    @BindView(R.id.federationRankLayout)
    public View federationRankLayout;
    @BindView(R.id.empireRankLayout)
    public View empireRankLayout;
    @BindView(R.id.combatRankLayout)
    public View combatRankLayout;
    @BindView(R.id.tradeRankLayout)
    public View tradeRankLayout;
    @BindView(R.id.explorationRankLayout)
    public View explorationRankLayout;
    @BindView(R.id.arenaRankLayout)
    public View arenaRankLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        ButterKnife.bind(this, v);

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getAll();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);

        // Set temporary text for credit and position text view during loading
        creditsTextView.setText(getResources().getString(R.string.credits, "?"));
        locationsTextView.setText(getResources().getString(R.string.Unknown));

        // Set card title to commander name
        String cmdrName = SettingsUtils.getCommanderName(this.getContext());
        commanderNameTextView.setText(cmdrName.length() == 0 ?
                getResources().getString(R.string.Unknown) : cmdrName);

        return v;
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
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        // Register event and get the news
        EventBus.getDefault().register(this);
        getAll();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onCreditsEvent(Credits credits) {
        endLoading();
        // Check download error
        if (credits == null)
        {
            NotificationsUtils.displayErrorSnackbar(getActivity());
        }

        // Check error case
        if (credits.balance == -1)
        {
            creditsTextView.setText(getResources().getString(R.string.Unknown));
            return;
        }
        String amount = NumberFormat.getIntegerInstance(Locale.FRENCH).format(credits.balance);
        if (credits.loan != 0) {
            String loan = NumberFormat.getIntegerInstance(Locale.FRENCH).format(credits.loan);
            creditsTextView.setText(getResources().getString(R.string.credits_with_loan, amount, loan));
        } else {
            creditsTextView.setText(getResources().getString(R.string.credits, amount));
        }
    }

    @Subscribe
    public void onPositionEvent(CommanderPosition position) {
        endLoading();
        // Check download error
        if (position == null)
        {
            NotificationsUtils.displayErrorSnackbar(getActivity());
        }

        // Check error case
        if (position.SystemName == null) {
            locationsTextView.setText(getResources().getString(R.string.Unknown));
        }
        else {
            locationsTextView.setText(position.SystemName);
        }
    }

    @Subscribe
    public void onRanksEvents(Ranks ranks) {
        endLoading();
        // Check download error
        if (ranks == null)
        {
            NotificationsUtils.displayErrorSnackbar(getActivity());
        }

        RankViewUtils.setContent(getContext(), federationRankLayout, R.drawable.elite_federation, ranks.federation.name, ranks.federation.progress, getString(R.string.rank_federation));
        RankViewUtils.setContent(getContext(), empireRankLayout, R.drawable.elite_empire, ranks.empire.name, ranks.empire.progress, getString(R.string.rank_empire));

        RankViewUtils.setContent(getContext(), combatRankLayout, RankViewUtils.getCombatLogoId(ranks.combat.value), ranks.combat.name, ranks.combat.progress, getString(R.string.rank_combat));
        RankViewUtils.setContent(getContext(), tradeRankLayout, RankViewUtils.getTradeLogoId(ranks.combat.value), ranks.trade.name, ranks.trade.progress, getString(R.string.rank_trading));
        RankViewUtils.setContent(getContext(), explorationRankLayout, RankViewUtils.getExplorationLogoId(ranks.explore.value), ranks.explore.name, ranks.explore.progress, getString(R.string.rank_exploration));
        RankViewUtils.setContent(getContext(), arenaRankLayout, RankViewUtils.getCqcLogoId(ranks.cqc.value), ranks.cqc.name, ranks.cqc.progress, getString(R.string.rank_arena));
    }

    private void getAll()
    {
        if (!SettingsUtils.hasValidCmdrParameters(getActivity())) {
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),
                            R.string.commander_error, Snackbar.LENGTH_SHORT);
            snackbar.show();
            endLoading();
        } else {
            PlayerStatusNetwork.getCredits(getContext());
            PlayerStatusNetwork.getRanks(getContext());
            PlayerStatusNetwork.getPosition(getContext());
        }
    }

    public void endLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
