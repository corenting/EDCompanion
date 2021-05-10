package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Instant;

import java.text.NumberFormat;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.databinding.ListItemCommodityDetailsStationBinding;
import fr.corenting.edcompanion.models.CommodityDetailsStationResult;
import fr.corenting.edcompanion.utils.MathUtils;

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
        ListItemCommodityDetailsStationBinding itemBinding = ListItemCommodityDetailsStationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new stationViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final stationViewHolder holder, final int position) {
        CommodityDetailsStationResult currentStation = getItem(holder.getAdapterPosition());

        holder.viewBinding.titleTextView.setText(String.format("%s - %s", currentStation.getStation().getSystemName(),
                currentStation.getStation().getName()));
        holder.viewBinding.typeTextView.setText(currentStation.getStation().getType());
        holder.viewBinding.starDistanceTextView.setText(context.getString(R.string.distance_ls,
                numberFormat.format(currentStation.getStation().getDistanceToStar())));
        holder.viewBinding.landingPadTextView.setText(currentStation.getStation().getMaxLandingPad());

        holder.viewBinding.logoImageView.setVisibility(currentStation.getStation().isPlanetary() ? View.VISIBLE : View.GONE);

        if (isSellMode) {
            holder.viewBinding.priceLabelTextView.setText(R.string.sell_price);
            holder.viewBinding.priceTextView.setText(context.getString(R.string.credits, numberFormat.format(currentStation.getSellPrice())));
            holder.viewBinding.stockLabelTextView.setText(R.string.demand_label);
            holder.viewBinding.stockTextView.setText(numberFormat.format(currentStation.getDemand()));
        } else {
            holder.viewBinding.priceLabelTextView.setText(R.string.buy_price);
            holder.viewBinding.priceTextView.setText(context.getString(R.string.credits, numberFormat.format(currentStation.getBuyPrice())));
            holder.viewBinding.stockLabelTextView.setText(R.string.stock_label);
            holder.viewBinding.stockTextView.setText(numberFormat.format(currentStation.getSupply()));
        }
        holder.viewBinding.priceDifferenceTextView.setText(String.format("%s %%",
                MathUtils.getPriceDifferenceString(numberFormat,
                        currentStation.getPriceDifferencePercentage())));

        // Update date
        String date = android.text.format.DateUtils.getRelativeTimeSpanString(
                currentStation.getCollectedAt().toEpochMilli(), Instant.now().toEpochMilli(),
                0, FORMAT_ABBREV_RELATIVE).toString();
        holder.viewBinding.lastUpdateTextView.setText(date);

    }

    public static class stationViewHolder extends RecyclerView.ViewHolder {
        private final ListItemCommodityDetailsStationBinding viewBinding;

        stationViewHolder(final ListItemCommodityDetailsStationBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
