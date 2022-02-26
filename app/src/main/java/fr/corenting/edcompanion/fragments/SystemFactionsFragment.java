package fr.corenting.edcompanion.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.SystemDetailsActivity;
import fr.corenting.edcompanion.databinding.FragmentSystemFactionsBinding;
import fr.corenting.edcompanion.models.Faction;
import fr.corenting.edcompanion.models.FactionChartEntryData;
import fr.corenting.edcompanion.models.FactionHistory;
import fr.corenting.edcompanion.models.System;
import fr.corenting.edcompanion.models.SystemHistoryResult;
import fr.corenting.edcompanion.models.events.SystemDetails;
import fr.corenting.edcompanion.models.events.SystemHistory;
import fr.corenting.edcompanion.utils.ColorUtils;
import fr.corenting.edcompanion.utils.DateUtils;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ThemeUtils;
import fr.corenting.edcompanion.views.GraphMarkerView;

public class SystemFactionsFragment extends Fragment {

    public static final String SYSTEM_FACTIONS_FRAGMENT = "system_factions_fragment";

    private FragmentSystemFactionsBinding binding;
    private Locale userLocale;
    private DateTimeFormatter dateFormatter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSystemFactionsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = () -> {
            binding.swipeContainer.setRefreshing(true);
            if (getActivity() != null) {
                ((SystemDetailsActivity) getActivity()).getData();
            }
        };
        binding.swipeContainer.setOnRefreshListener(listener);

        // Setup views
        binding.swipeContainer.setVisibility(View.VISIBLE);
        binding.swipeContainer.setRefreshing(true);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        userLocale = DateUtils.getCurrentLocale(getContext());
        dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSystemEvent(SystemDetails systemDetails) {
        endLoading();

        if (systemDetails.getSuccess() && systemDetails.getSystem() != null) {
            bindInformations(systemDetails.getSystem());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHistoryEvent(SystemHistory systemHistory) {
        endLoading();

        try {
            if (systemHistory.getSuccess()) {
                bindHistoryChart(systemHistory.getHistory());
                binding.chartProgressBar.setVisibility(View.GONE);
                binding.historyChartView.setVisibility(View.VISIBLE);
                binding.chartErrorTextView.setVisibility(View.GONE);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            if (getActivity() != null) {
                NotificationsUtils.displaySnackbar(getActivity(),
                        this.getString(R.string.no_chart_snackbar_error));
            }
            binding.chartProgressBar.setVisibility(View.GONE);
            binding.historyChartView.setVisibility(View.GONE);
            binding.chartErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    public void endLoading() {
        binding.swipeContainer.setRefreshing(false);
    }

    private void bindHistoryChart(List<SystemHistoryResult> history) {
        binding.historyChartView.setVisibility(history.size() == 0 ? View.GONE : View.VISIBLE);
        if (history.size() == 0) {
            return;
        }

        // Prevent scrolling being intercepted by viewpager
        BarLineChartTouchListener touchListener = new BarLineChartTouchListener(binding.historyChartView,
                binding.historyChartView.getViewPortHandler().getMatrixTouch(), 3F) {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    default:
                        break;
                }
                return super.onTouch(v, event);
            }
        };
        binding.historyChartView.setOnTouchListener(touchListener);

        // Modify data for graph
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(1);
        List<LocalDate> dates = new ArrayList<>();
        while (end.isAfter(start)) {
            dates.add(start);
            start = start.plusDays(1);
        }

        // For each faction, get history entry for each day of the dates list
        Map<String, Entry[]> sets = new HashMap<>();
        for (SystemHistoryResult factionEntry : history) {
            // Check if faction has history
            List<FactionHistory> factionHistory = factionEntry.getHistory();
            if (factionHistory.size() == 0) {
                continue;
            }

            // Get entries for this faction
            Entry[] entries = sets.get(factionEntry.getName());
            if (entries == null) {
                entries = new Entry[dates.size() + 1];
            }

            // For each date in history, add to entry
            for (FactionHistory historyItem : factionHistory) {

                // Get index of date for array
                int dateIndex;
                for (dateIndex = 0; dateIndex < dates.size(); dateIndex++) {
                    LocalDate date = dates.get(dateIndex);
                    LocalDate updateDate = historyItem.getUpdateDate().atOffset(ZoneOffset.UTC)
                            .toLocalDate();

                    if (updateDate.equals(date)) {
                        break;
                    }
                }

                FactionChartEntryData entryData = new FactionChartEntryData(
                        factionEntry.getName(), historyItem.getState(),
                        historyItem.getUpdateDate(), historyItem.getInfluence());
                entries[dateIndex] = new Entry(dateIndex, historyItem.getInfluence() * 100
                        , entryData);
            }

            sets.put(factionEntry.getName(), entries);
        }

        // Build data sets
        List<Integer> colors = ColorUtils.getUniqueColorsList(history.size());
        List<LineDataSet> dataSets = new ArrayList<>();
        int colorIndex = 0;
        for (Map.Entry<String, Entry[]> entry : sets.entrySet()) {
            // Remove empty entry if a date is missing
            List<Entry> entries = new LinkedList<>(Arrays.asList(entry.getValue()));
            entries.removeAll(Collections.singleton(null));

            // Create set
            LineDataSet set = new LineDataSet(entries, entry.getKey());
            int currentColor = colors.get(colorIndex);
            set.setColor(currentColor);
            set.setCircleColor(currentColor);
            set.setDrawHighlightIndicators(false);
            dataSets.add(set);

            colorIndex++;
        }

        // Data
        LineDataSet[] dataSetsArray = dataSets.toArray(new LineDataSet[0]);
        LineData data = new LineData(dataSetsArray);
        data.setDrawValues(false);
        binding.historyChartView.setData(data);

        // Empty description to hide default label
        Description desc = new Description();
        desc.setText("");
        binding.historyChartView.setDescription(desc);

        // Color for dark theme
        if (ThemeUtils.isDarkThemeEnabled(getContext())) {
            int color = Color.parseColor("#ffffff");
            binding.historyChartView.getAxisLeft().setTextColor(color);
            binding.historyChartView.getXAxis().setTextColor(color);
            binding.historyChartView.getLegend().setTextColor(color);
        }


        // Misc params
        binding.historyChartView.getAxisRight().setEnabled(false);
        binding.historyChartView.setExtraLeftOffset(10);
        binding.historyChartView.setExtraRightOffset(50);
        binding.historyChartView.getLegend().setWordWrapEnabled(true);

        // Marker view
        GraphMarkerView graphMarker = new GraphMarkerView(getContext());
        graphMarker.setChartView(binding.historyChartView);
        binding.historyChartView.setMarker(graphMarker);

        // Labels
        final SparseArray<String> labels = new SparseArray<>();
        for (int i = 0; i < dates.size(); i++) {
            labels.put(i, DateUtils.removeYearFromDate(
                    dateFormatter.format(dates.get(i))));
        }
        binding.historyChartView.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels.get((int) value, "");
            }
        });
        binding.historyChartView.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + " %";
            }
        });
        binding.historyChartView.notifyDataSetChanged();
        binding.historyChartView.invalidate();
    }

    private void bindInformations(System system) {
        // Current state
        binding.controllingFactionTextView.setText(system.getControllingFaction() != null ? system.getControllingFaction() : getString(R.string.unknown));
        binding.allegianceTextView.setText(system.getAllegiance() != null ? system.getAllegiance() : getString(R.string.unknown));
        binding.powerTextView.setText(system.getPower() != null ? String.format("%s (%s)", system.getPower(), system.getPowerState()) : getString(R.string.unknown));

        // Factions list
        if (system.getFactions().size() == 0) {
            binding.factionsListTextView.setText(getString(R.string.no_factions));
            return;
        }
        binding.factionsListTextView.setText(Html.fromHtml(getHtmlFactionsList(system.getFactions())));
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

            // Happiness
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            stringBuilder.append("<i>");
            stringBuilder.append(getString(R.string.happiness_label));
            stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</i>");
            stringBuilder.append(faction.getHappiness());
            stringBuilder.append("<br />");
        }
        return stringBuilder.toString();
    }
}
