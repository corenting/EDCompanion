package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
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
import fr.corenting.edcompanion.network.StatusNetwork;
import fr.corenting.edcompanion.utils.RankViewUtils;

public class StatusFragment extends Fragment  {

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;

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
        final StatusFragment parent = this;
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                StatusNetwork.getAll(parent);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);

        // Set temporary text for credit text view during loading
        creditsTextView.setText(getResources().getString(R.string.credits, "?"));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup views
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        // Register event and get the news
        EventBus.getDefault().register(this);
        StatusNetwork.getAll(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onCreditsEvent(Credits credits) {
        String amount = NumberFormat.getIntegerInstance(Locale.FRENCH).format(credits.balance);
        creditsTextView.setText(getResources().getString(R.string.credits, amount));
    }

    @Subscribe
    public void onPositionEvent(CommanderPosition position) {
        locationsTextView.setText(position.SystemName);
    }

    @Subscribe
    public void onRanksEvents(Ranks ranks) {
        RankViewUtils.setContent(getContext(), federationRankLayout, R.drawable.elite_federation, ranks.federation.name, ranks.federation.progress);
        RankViewUtils.setContent(getContext(), empireRankLayout, R.drawable.elite_empire, ranks.empire.name, ranks.empire.progress);

        RankViewUtils.setContent(getContext(), combatRankLayout, RankViewUtils.getCombatLogoId(ranks.combat.value), ranks.combat.name, ranks.combat.progress);
        RankViewUtils.setContent(getContext(), tradeRankLayout, RankViewUtils.getTradeLogoId(ranks.combat.value), ranks.trade.name, ranks.trade.progress);
        RankViewUtils.setContent(getContext(), explorationRankLayout, RankViewUtils.getExplorationLogoId(ranks.explore.value), ranks.explore.name, ranks.explore.progress);
        RankViewUtils.setContent(getContext(), arenaRankLayout, RankViewUtils.getCqcLogoId(ranks.cqc.value), ranks.cqc.name, ranks.cqc.progress);
    }

    public void endLoading()
    {
        swipeRefreshLayout.setRefreshing(false);
    }
}
