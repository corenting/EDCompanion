package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.SystemDetailsActivity;
import fr.corenting.edcompanion.models.Faction;
import fr.corenting.edcompanion.models.System;
import fr.corenting.edcompanion.models.SystemHistoryResult;
import fr.corenting.edcompanion.models.events.CommanderPosition;
import fr.corenting.edcompanion.models.events.Credits;
import fr.corenting.edcompanion.models.events.Ranks;
import fr.corenting.edcompanion.models.events.SystemDetails;
import fr.corenting.edcompanion.models.events.SystemHistory;
import fr.corenting.edcompanion.network.SystemNetwork;
import fr.corenting.edcompanion.network.player.PlayerNetwork;
import fr.corenting.edcompanion.utils.DateUtils;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.PlayerNetworkUtils;
import fr.corenting.edcompanion.utils.RankUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;

public class SystemFactionsFragment extends Fragment {

    public static final String SYSTEM_FACTIONS_FRAGMENT = "system_factions_fragment";

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.allegianceTextView)
    public TextView allegianceTextView;
    @BindView(R.id.powerTextView)
    public TextView powerTextView;
    @BindView(R.id.controllingFactionTextView)
    public TextView controllingFactionTextView;
    @BindView(R.id.factionsListTextView)
    public TextView factionsListTextView;
    @BindView(R.id.historyChartView)
    public AnyChartView historyChartView;

    private Locale userLocale;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_factions, container, false);
        ButterKnife.bind(this, v);

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (getActivity() != null) {
                    ((SystemDetailsActivity) getActivity()).getData();
                }
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);

        // Setup views
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        userLocale = DateUtils.getCurrentLocale(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        // Register event and get the news
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSystemEvent(SystemDetails systemDetails) {
        endLoading();

        if (systemDetails.getSuccess() && systemDetails.getSystem() != null) {
            bindInformations(systemDetails.getSystem());
        }
    }

    @Subscribe
    public void onSystemEvent(SystemHistory systemHistory) {
        endLoading();

        if (systemHistory.getSuccess()) {
            bindHistoryChart(systemHistory.getHistory());
        }
    }

    private void bindHistoryChart(List<SystemHistoryResult> history) {
        historyChartView.setVisibility(history.size() == 0 ? View.GONE : View.VISIBLE);
        if (history.size() == 0) {
            return;
        }

        // Chart parameters
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.yAxis(0).title(getString(R.string.influence));
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        // Chart data
        // TODO

        historyChartView.setChart(cartesian);
    }

    private void bindInformations(System system) {
        // Current state
        controllingFactionTextView.setText(system.getControllingFaction());
        allegianceTextView.setText(system.getAllegiance());
        powerTextView.setText(String.format("%s (%s)", system.getPower(), system.getPowerState()));

        // Factions list
        if (system.getFactions().size() == 0) {
            factionsListTextView.setText(getString(R.string.no_factions));
            return;
        }
        factionsListTextView.setText(Html.fromHtml(getHtmlFactionsList(system.getFactions())));
    }

    private String getHtmlFactionsList(List<Faction> factions) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Faction faction : factions) {
            // Title
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>");
            stringBuilder.append(faction.getName());
            stringBuilder.append("</b><br />");

            // Influence
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            stringBuilder.append("<i>");
            stringBuilder.append(getString(R.string.influence));
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</i>");
            stringBuilder.append(String.format(userLocale, "%.2f",
                    faction.getInfluence() * 100));
            stringBuilder.append("%<br />");

            // Allegiance
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            stringBuilder.append("<i>");
            stringBuilder.append(getString(R.string.allegiance_label));
            stringBuilder.append("&nbsp;&nbsp;&nbsp&nbsp&nbsp;&nbsp;</i>");
            stringBuilder.append(faction.getAllegiance());
            stringBuilder.append("<br />");

            // Government
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            stringBuilder.append("<i>");
            stringBuilder.append(getString(R.string.government_label));
            stringBuilder.append("&nbsp;&nbsp;&nbsp;</i>");
            stringBuilder.append(faction.getGovernment());
            stringBuilder.append("<br />");

            // State
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            stringBuilder.append("<i>");
            stringBuilder.append(getString(R.string.state_label));
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</i>");
            stringBuilder.append(faction.getState());
            stringBuilder.append("<br />");
        }
        return stringBuilder.toString();
    }

    public void endLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
