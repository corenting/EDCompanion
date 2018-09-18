package fr.corenting.edcompanion.views;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.FactionChartEntryData;

public class GraphMarkerView extends MarkerView {

    private final DateTimeFormatter dateFormatter;

    private TextView factionNameTextView;
    private TextView influenceTextView;
    private TextView stateTextView;
    private TextView updateDateTextView;

    public GraphMarkerView(Context context) {
        super(context, R.layout.view_marker);

        factionNameTextView = findViewById(R.id.factionNameTextView);
        influenceTextView = findViewById(R.id.influenceTextView);
        stateTextView = findViewById(R.id.stateTextView);
        updateDateTextView = findViewById(R.id.updateDateTextView);

        dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        FactionChartEntryData historyItem = (FactionChartEntryData) e.getData();
        factionNameTextView.setText(historyItem.getName());

        influenceTextView.setText(String.format("%s %%", (int) (historyItem.getInfluence() * 100)));
        stateTextView.setText(historyItem.getState());

        LocalDateTime date = LocalDateTime.ofInstant(historyItem.getUpdateDate(), ZoneOffset.UTC);
        updateDateTextView.setText(dateFormatter.format(date));

        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}