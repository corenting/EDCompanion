package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Instant;

import java.text.NumberFormat;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.databinding.FragmentFindCommodityHeaderBinding;
import fr.corenting.edcompanion.databinding.ListItemCommodityFinderResultBinding;
import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.events.CommodityFinderSearch;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.utils.ViewUtils;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

public class CommodityFinderAdapter extends FinderAdapter<CommodityFinderAdapter.HeaderViewHolder, CommodityFinderAdapter.ResultViewHolder, CommodityFinderResult> {

    private final NumberFormat numberFormat;
    private boolean isSellingMode = false;

    public CommodityFinderAdapter(Context context) {
        super(context);
        numberFormat = MathUtils.getNumberFormat(context);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NotNull ViewGroup parent) {
        FragmentFindCommodityHeaderBinding headerBinding = FragmentFindCommodityHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HeaderViewHolder(headerBinding);
    }

    @Override
    protected RecyclerView.ViewHolder getResultViewHolder(@NonNull @NotNull ViewGroup parent) {
        ListItemCommodityFinderResultBinding resultBinding = ListItemCommodityFinderResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ResultViewHolder(resultBinding);
    }

    @Override
    protected void bindHeaderViewHolder(final HeaderViewHolder holder) {
        // Ship autocomplete
        holder.binding.commodityInputEditText.setThreshold(3);
        holder.binding.commodityInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_COMMODITIES));
        holder.binding.commodityInputEditText.setOnItemClickListener((adapterView, view, position, id) ->
                holder.binding.commodityInputEditText.setText((String) adapterView.getItemAtPosition(position)));

        // Landing pad size adapter
        if (holder.binding.landingPadSizeAutoCompleteTextView.getAdapter() == null ||
                holder.binding.landingPadSizeAutoCompleteTextView.getAdapter().getCount() == 0) {
            String[] landingPadSizeArray = context.getResources()
                    .getStringArray(R.array.landing_pad_size);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_dropdown_item_1line, landingPadSizeArray);
            holder.binding.landingPadSizeAutoCompleteTextView.setAdapter(adapter);
        }

        // Buy or sell adapter
        if (holder.binding.buyOrSellAutoCompleteTextView.getAdapter() == null ||
                holder.binding.buyOrSellAutoCompleteTextView.getAdapter().getCount() == 0) {
            String[] buySellArray = context.getResources()
                    .getStringArray(R.array.buy_sell_array);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_dropdown_item_1line, buySellArray);
            holder.binding.buyOrSellAutoCompleteTextView.setAdapter(adapter);

            holder.binding.buyOrSellAutoCompleteTextView.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                holder.binding.stockInputLayout.setHint(context
                                        .getString(R.string.minimum_stock));
                                holder.binding.stockInputEditText.setHint(context
                                        .getString(R.string.minimum_stock));
                            } else {
                                holder.binding.stockInputLayout.setHint(context
                                        .getString(R.string.minimum_demand));
                                holder.binding.stockInputEditText.setHint(context
                                        .getString(R.string.minimum_demand));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    }
            );
        }

        // Find button
        final Runnable onSubmit = () -> {
            if (!findButtonEnabled) {
                return;
            }

            ViewUtils.hideSoftKeyboard(holder.binding.findButton.getRootView());

            // Convert stock value to int
            String stockString = holder.binding.stockInputEditText.getText().toString();
            int stock = 0;
            if (stockString.length() != 0) {
                try {
                    stock = Integer.parseInt(stockString);
                } catch (NumberFormatException ignored) {
                }
            }

            // Convert buy/sell mode to boolean
            String[] buySellArray = context.getResources()
                    .getStringArray(R.array.buy_sell_array);
            isSellingMode = holder.binding.buyOrSellAutoCompleteTextView.getText().toString().equals(buySellArray[1]);

            CommodityFinderSearch result = new CommodityFinderSearch(
                    holder.binding.commodityInputEditText.getText().toString(),
                    holder.binding.systemInputView.getText().toString(),
                    holder.binding.landingPadSizeAutoCompleteTextView.getText().toString(),
                    stock, isSellingMode);

            EventBus.getDefault().post(result);
        };

        // On submit stuff
        holder.binding.findButton.setOnClickListener(view -> onSubmit.run());
        holder.binding.stockInputEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                onSubmit.run();
                return true;
            }
            return false;
        });
        holder.binding.commodityInputEditText.setOnSubmit(onSubmit);
        holder.binding.systemInputView.setOnSubmit(onSubmit);
    }

    @Override
    protected void bindResultViewHolder(ResultViewHolder holder, int position) {
        CommodityFinderResult currentResult = results.get(position - 1);

        // For price, also display the difference with the avg galactic price
        String priceDifference = MathUtils.getPriceDifferenceString(
                numberFormat,
                currentResult.getPriceDifferenceFromAverage());
        if (isSellingMode) {
            String sellPrice = numberFormat.format(currentResult.getSellPrice());
            holder.binding.priceTextView.setText(String.format("%s (%s%%)", sellPrice, priceDifference));
        } else {
            String buyPrice = numberFormat.format(currentResult.getBuyPrice());
            holder.binding.priceTextView.setText(String.format("%s (%s%%)", buyPrice, priceDifference));
        }


        // Update date
        String date = android.text.format.DateUtils.getRelativeTimeSpanString(
                currentResult.getLastPriceUpdate().toEpochMilli(), Instant.now().toEpochMilli(),
                0, FORMAT_ABBREV_RELATIVE).toString();
        holder.binding.lastUpdateTextView.setText(date);

        // Other informations
        holder.binding.titleTextView.setText(String.format("%s - %s", currentResult.getSystem(),
                currentResult.getStation()));
        holder.binding.permitRequiredTextView.setVisibility(
                currentResult.isPermitRequired() ? View.VISIBLE : View.GONE);
        holder.binding.isPlanetaryImageView.setVisibility(
                currentResult.isPlanetary() ? View.VISIBLE : View.GONE);
        holder.binding.landingPadTextView.setText(currentResult.getLandingPad());
        holder.binding.distanceTextView.setText(context.getString(R.string.distance_ly,
                currentResult.getDistance()));
        holder.binding.distanceToStarTextView.setText(context.getString(R.string.distance_ls, numberFormat
                .format(currentResult.getDistanceToStar())));

        // Set stock/demand depending on mode
        if (isSellingMode) {
            holder.binding.stockLabelTextView.setText(R.string.demand_label);
            holder.binding.stockTextView.setText(numberFormat.format(currentResult.getDemand()));
        } else {
            holder.binding.stockLabelTextView.setText(R.string.stock_label);
            holder.binding.stockTextView.setText(numberFormat.format(currentResult.getStock()));
        }
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        private final ListItemCommodityFinderResultBinding binding;

        public ResultViewHolder(ListItemCommodityFinderResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final FragmentFindCommodityHeaderBinding binding;

        public HeaderViewHolder(FragmentFindCommodityHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}