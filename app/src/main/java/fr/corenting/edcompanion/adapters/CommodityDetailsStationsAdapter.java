package fr.corenting.edcompanion.adapters;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

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
import fr.corenting.edcompanion.models.CommodityBestPricesStationResult;
import fr.corenting.edcompanion.utils.MathUtils;

public class CommodityDetailsStationsAdapter extends androidx.recyclerview.widget.ListAdapter<CommodityBestPricesStationResult,
        CommodityDetailsStationsAdapter.stationViewHolder> {

    private final NumberFormat numberFormat;
    private final boolean isSellMode;
    private final Context context;


    public CommodityDetailsStationsAdapter(Context ctx, boolean isSellMode) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<CommodityBestPricesStationResult>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommodityBestPricesStationResult oldItem,
                                           @NonNull CommodityBestPricesStationResult newItem) {
                return areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommodityBestPricesStationResult oldItem,
                                              @NonNull CommodityBestPricesStationResult newItem) {
                return oldItem.getStationName().equals(newItem.getStationName()) &&
                        oldItem.getSystemName().equals(newItem.getSystemName());
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
        CommodityBestPricesStationResult currentStation = getItem(holder.getAdapterPosition());

        holder.viewBinding.titleTextView.setText(String.format("%s - %s", currentStation.getSystemName(),
                currentStation.getStationName()));
        holder.viewBinding.typeTextView.setText(currentStation.getStationType());
        holder.viewBinding.starDistanceTextView.setText(context.getString(R.string.distance_ls,
                numberFormat.format(currentStation.getDistanceFromArrival())));
        holder.viewBinding.landingPadTextView.setText(currentStation.getMaxLandingPadSize());

        holder.viewBinding.logoImageView.setVisibility(currentStation.getStationIsPlanetary() || currentStation.getStationIsSettlement() ? View.VISIBLE : View.GONE);

        holder.viewBinding.priceTextView.setText(context.getString(R.string.credits, numberFormat.format(currentStation.getPrice())));
        holder.viewBinding.stockTextView.setText(numberFormat.format(currentStation.getQuantity()));
        if (isSellMode) {
            holder.viewBinding.priceLabelTextView.setText(R.string.sell_price);
            holder.viewBinding.stockLabelTextView.setText(R.string.demand_label);
        } else {
            holder.viewBinding.priceLabelTextView.setText(R.string.buy_price);
            holder.viewBinding.stockLabelTextView.setText(R.string.stock_label);
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
