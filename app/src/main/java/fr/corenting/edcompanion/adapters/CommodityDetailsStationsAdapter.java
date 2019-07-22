package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Instant;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommodityDetailsStationResult;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.views.LightDarkImageView;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

public class CommodityDetailsStationsAdapter extends androidx.recyclerview.widget.ListAdapter<CommodityDetailsStationResult,
        CommodityDetailsStationsAdapter.stationViewHolder> {

    private final NumberFormat numberFormat;
    private final boolean isSellMode;
    private Context context;


    public CommodityDetailsStationsAdapter(Context ctx, boolean isSellMode) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<CommodityDetailsStationResult>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommodityDetailsStationResult oldItem,
                                           @NonNull CommodityDetailsStationResult newItem) {
                return areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommodityDetailsStationResult oldItem,
                                              @NonNull CommodityDetailsStationResult newItem) {
                return oldItem.getStation().getName().equals(newItem.getStation().getName()) &&
                        oldItem.getStation().getSystemName().equals(newItem.getStation().getSystemName());
            }
        });

        this.context = ctx;
        this.numberFormat = MathUtils.getNumberFormat(ctx);
        this.isSellMode = isSellMode;
    }

    @NonNull
    @Override
    public stationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_commodity_details_station,
                parent, false);
        return new stationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final stationViewHolder holder, final int position) {
        CommodityDetailsStationResult currentStation = getItem(holder.getAdapterPosition());

        holder.titleTextView.setText(String.format("%s - %s", currentStation.getStation().getSystemName(),
                currentStation.getStation().getName()));
        holder.typeTextView.setText(currentStation.getStation().getType());
        holder.starDistanceTextView.setText(context.getString(R.string.distance_ls,
                numberFormat.format(currentStation.getStation().getDistanceToStar())));
        holder.landingPadTextView.setText(currentStation.getStation().getMaxLandingPad());

        holder.logoImageView.setVisibility(currentStation.getStation().isPlanetary() ? View.VISIBLE : View.GONE);

        if (isSellMode) {
            holder.priceLabelTextView.setText(R.string.sell_price);
            holder.priceTextView.setText(context.getString(R.string.credits, numberFormat.format(currentStation.getSellPrice())));
            holder.stockLabelTextView.setText(R.string.demand_label);
            holder.stockTextView.setText(numberFormat.format(currentStation.getDemand()));
        } else {
            holder.priceLabelTextView.setText(R.string.buy_price);
            holder.priceTextView.setText(context.getString(R.string.credits, numberFormat.format(currentStation.getBuyPrice())));
            holder.stockLabelTextView.setText(R.string.stock_label);
            holder.stockTextView.setText(numberFormat.format(currentStation.getSupply()));
        }
        holder.priceDifferenceTextView.setText(String.format("%s %%",
                MathUtils.getPriceDifferenceString(numberFormat,
                        currentStation.getPriceDifferencePercentage())));

        // Update date
        String date = android.text.format.DateUtils.getRelativeTimeSpanString(
                currentStation.getCollectedAt().toEpochMilli(), Instant.now().toEpochMilli(),
                0, FORMAT_ABBREV_RELATIVE).toString();
        holder.lastUpdateTextView.setText(date);

    }

    public static class stationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.logoImageView)
        LightDarkImageView logoImageView;

        @BindView(R.id.priceLabelTextView)
        TextView priceLabelTextView;
        @BindView(R.id.priceTextView)
        TextView priceTextView;
        @BindView(R.id.stockLabelTextView)
        TextView stockLabelTextView;
        @BindView(R.id.stockTextView)
        TextView stockTextView;
        @BindView(R.id.priceDifferenceTextView)
        TextView priceDifferenceTextView;
        @BindView(R.id.lastUpdateTextView)
        TextView lastUpdateTextView;

        @BindView(R.id.typeTextView)
        TextView typeTextView;
        @BindView(R.id.starDistanceTextView)
        TextView starDistanceTextView;
        @BindView(R.id.landingPadTextView)
        TextView landingPadTextView;

        stationViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
