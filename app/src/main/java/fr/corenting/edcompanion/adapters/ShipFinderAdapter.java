package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.threeten.bp.Instant;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.ShipFinderFragment;
import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.utils.SettingsUtils;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

public class ShipFinderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private ShipFinderFragment shipFinderFragment;
    private List<ShipFinderResult> results;
    private TextView emptyTextView;

    public ShipFinderAdapter(final Context context, final ShipFinderFragment shipFinderFragment) {
        this.context = context;
        this.shipFinderFragment = shipFinderFragment;
        this.results = new LinkedList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_find_ship_header,
                    parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ship_finder_list_item,
                    parent, false);
            return new ResultViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder header = (HeaderViewHolder) holder;

            // System autocomplete
            header.systemInputEditText.setThreshold(3);
            header.systemInputEditText.setLoadingIndicator(header.systemProgressBar);

            header.systemInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS));
            header.systemInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    header.systemInputEditText.setText((String) adapterView.getItemAtPosition(position));
                }
            });

            // Ship autocomplete
            header.shipInputEditText.setThreshold(3);
            header.shipInputEditText.setLoadingIndicator(header.shipProgressBar);
            header.shipInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SHIPS));
            header.shipInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    header.shipInputEditText.setText((String) adapterView.getItemAtPosition(position));
                }
            });

            // Find button
            header.findButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shipFinderFragment.onFindButtonClick((Button) view,
                            header.systemInputEditText.getText().toString(),
                            header.shipInputEditText.getText().toString());
                }
            });

            // Bind empty text view
            emptyTextView = header.emptyText;

        } else {
            ShipFinderResult currentResult = results.get(position - 1);
            final ResultViewHolder resultViewHolder = (ResultViewHolder) holder;

            // Title, landing pad, station type
            resultViewHolder.titleTextView.setText(
                    String.format("%s - %s", currentResult.SystemName, currentResult.StationName)
            );
            resultViewHolder.landingPadTextView.setText(currentResult.MaxLandingPad);
            resultViewHolder.stationTypeTextView.setText(currentResult.Type);

            // Distance and distance to star
            resultViewHolder.distanceTextView.setText(context.getString(R.string.distance_ly,
                    currentResult.Distance));
            resultViewHolder.starDistanceTextView.setText(context.getString(R.string.distance_ls,
                    NumberFormat.getIntegerInstance(SettingsUtils.getUserLocale(context)).format(currentResult.DistanceToStar)));

            // Update date
            String date = android.text.format.DateUtils.getRelativeTimeSpanString(currentResult.LastShipyardUpdate.toEpochMilli(),
                    Instant.now().toEpochMilli(), 0, FORMAT_ABBREV_RELATIVE).toString();
            resultViewHolder.lastUpdateTextView.setText(date);

            // Permit state
            if (currentResult.SystemPermitRequired)
            {
                resultViewHolder.permitRequiredTextView.setVisibility(View.VISIBLE);
            }
            else
            {
                resultViewHolder.permitRequiredTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return results.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isHeader(int position) {
        return position == 0;
    }

    public void setResults(List<ShipFinderResult> newResults) {
        results.clear();
        results = newResults;
        notifyDataSetChanged();
    }

    public View getEmptyTextView() {
        return emptyTextView;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.distanceTextView)
        TextView distanceTextView;

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.permitRequiredTextView)
        TextView permitRequiredTextView;

        @BindView(R.id.lastUpdateTextView)
        TextView lastUpdateTextView;

        @BindView(R.id.landingPadTextView)
        TextView landingPadTextView;

        @BindView(R.id.starDistanceTextView)
        TextView starDistanceTextView;

        @BindView(R.id.stationTypeTextView)
        TextView stationTypeTextView;

        public ResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.systemInputEditText)
        DelayAutoCompleteTextView systemInputEditText;

        @BindView(R.id.shipInputEditText)
        DelayAutoCompleteTextView shipInputEditText;

        @BindView(R.id.systemProgressBar)
        MaterialProgressBar systemProgressBar;

        @BindView(R.id.shipProgressBar)
        MaterialProgressBar shipProgressBar;

        @BindView(R.id.findButton)
        Button findButton;

        @BindView(R.id.emptyText)
        TextView emptyText;

        public HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
