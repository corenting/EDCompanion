package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.Instant;

import java.text.NumberFormat;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.events.CommodityFinderSearch;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import fr.corenting.edcompanion.views.ClickToSelectEditText;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;
import fr.corenting.edcompanion.views.LightDarkImageView;
import fr.corenting.edcompanion.views.SystemInputView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

public class CommodityFinderAdapter extends FinderAdapter<CommodityFinderAdapter.HeaderViewHolder, CommodityFinderAdapter.ResultViewHolder, CommodityFinderResult> {

    private final NumberFormat numberFormat;
    private boolean isSellingMode = false;

    public CommodityFinderAdapter(Context context) {
        super(context);
        numberFormat = MathUtils.getNumberFormat(context);
    }

    @Override
    protected RecyclerView.ViewHolder getNewHeaderViewHolder(View v) {
        return new HeaderViewHolder(v);
    }

    @Override
    protected RecyclerView.ViewHolder getResultViewHolder(View v) {
        return new ResultViewHolder(v);
    }

    @Override
    public TextView getEmptyTextView() {
        return getHeader().emptyText;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.fragment_find_commodity_header;
    }

    @Override
    protected int getResultResId() {
        return R.layout.list_item_commodity_finder_result;
    }

    @Override
    protected void bindHeaderViewHolder(final HeaderViewHolder holder) {
        // Ship autocomplete
        holder.commodityInputEditText.setThreshold(3);
        holder.commodityInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_COMMODITIES));
        holder.commodityInputEditText.setOnItemClickListener((adapterView, view, position, id) ->
                holder.commodityInputEditText.setText((String) adapterView.getItemAtPosition(position)));

        // Landing pad size adapter
        if (holder.landingPadSizeSpinner.getItems() == null ||
                holder.landingPadSizeSpinner.getItems().size() == 0) {
            String[] landingPadSizeArray = context.getResources()
                    .getStringArray(R.array.landing_pad_size);
            holder.landingPadSizeSpinner.setItems(Arrays.asList(landingPadSizeArray));
        }

        // Buy or sell adapter
        if (holder.buyOrSellSpinner.getItems() == null ||
                holder.buyOrSellSpinner.getItems().size() == 0) {
            String[] buySellArray = context.getResources()
                    .getStringArray(R.array.buy_sell_array);
            holder.buyOrSellSpinner.setItems(Arrays.asList(buySellArray));
            holder.buyOrSellSpinner.setOnItemSelectedListener(
                    (item, selectedIndex) -> {
                        if (selectedIndex == 0) {
                            holder.stockInputLayout.setHint(context
                                    .getString(R.string.minimum_stock));
                            holder.stockInputEditText.setHint(context
                                    .getString(R.string.minimum_stock));
                        } else {
                            holder.stockInputLayout.setHint(context
                                    .getString(R.string.minimum_demand));
                            holder.stockInputEditText.setHint(context
                                    .getString(R.string.minimum_demand));
                        }
                    });
        }

        // Find button
        final Runnable onSubmit = () -> {
            if (!findButtonEnabled) {
                return;
            }

            ViewUtils.hideSoftKeyboard(holder.findButton.getRootView());

            // Convert stock value to int
            String stockString = holder.stockInputEditText.getText().toString();
            int stock = 0;
            if (stockString.length() != 0) {
                try {
                    stock = Integer.parseInt(stockString);
                } catch (NumberFormatException ignored) {
                }
            }

            // Convert buy/sell mode to boolean
            isSellingMode = holder.buyOrSellSpinner.getSelectedIndex() == 1;

            CommodityFinderSearch result = new CommodityFinderSearch(
                    holder.commodityInputEditText.getText().toString(),
                    holder.systemInputView.getText().toString(),
                    holder.landingPadSizeSpinner.getText().toString(),
                    stock, isSellingMode);

            EventBus.getDefault().post(result);
        };

        // On submit stuff
        holder.findButton.setOnClickListener(view -> onSubmit.run());
        holder.stockInputEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                onSubmit.run();
                return true;
            }
            return false;
        });
        holder.commodityInputEditText.setOnSubmit(onSubmit);
        holder.systemInputView.setOnSubmit(onSubmit);
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
            holder.priceTextView.setText(String.format("%s (%s%%)", sellPrice, priceDifference));
        } else {
            String buyPrice = numberFormat.format(currentResult.getBuyPrice());
            holder.priceTextView.setText(String.format("%s (%s%%)", buyPrice, priceDifference));
        }


        // Update date
        String date = android.text.format.DateUtils.getRelativeTimeSpanString(
                currentResult.getLastPriceUpdate().toEpochMilli(), Instant.now().toEpochMilli(),
                0, FORMAT_ABBREV_RELATIVE).toString();
        holder.lastUpdateTextView.setText(date);

        // Other informations
        holder.titleTextView.setText(String.format("%s - %s", currentResult.getSystem(),
                currentResult.getStation()));
        holder.permitRequiredTextView.setVisibility(
                currentResult.isPermitRequired() ? View.VISIBLE : View.GONE);
        holder.isPlanetaryImageView.setVisibility(
                currentResult.isPlanetary() ? View.VISIBLE : View.GONE);
        holder.landingPadTextView.setText(currentResult.getLandingPad());
        holder.distanceTextView.setText(context.getString(R.string.distance_ly,
                currentResult.getDistance()));
        holder.distanceToStarTextView.setText(context.getString(R.string.distance_ls, numberFormat
                .format(currentResult.getDistanceToStar())));

        // Set stock/demand depending on mode
        if (isSellingMode) {
            holder.stockLabelTextView.setText(R.string.demand_label);
            holder.stockTextView.setText(numberFormat.format(currentResult.getDemand()));
        } else {
            holder.stockLabelTextView.setText(R.string.stock_label);
            holder.stockTextView.setText(numberFormat.format(currentResult.getStock()));
        }
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.isPlanetaryImageView)
        LightDarkImageView isPlanetaryImageView;

        @BindView(R.id.permitRequiredTextView)
        TextView permitRequiredTextView;

        @BindView(R.id.distanceTextView)
        TextView distanceTextView;

        @BindView(R.id.distanceToStarTextView)
        TextView distanceToStarTextView;

        @BindView(R.id.stockLabelTextView)
        TextView stockLabelTextView;

        @BindView(R.id.stockTextView)
        TextView stockTextView;

        @BindView(R.id.priceTextView)
        TextView priceTextView;

        @BindView(R.id.landingPadTextView)
        TextView landingPadTextView;

        @BindView(R.id.lastUpdateTextView)
        TextView lastUpdateTextView;

        public ResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.systemInputView)
        SystemInputView systemInputView;

        @BindView(R.id.commodityInputEditText)
        DelayAutoCompleteTextView commodityInputEditText;

        @BindView(R.id.landingPadSizeSpinner)
        ClickToSelectEditText landingPadSizeSpinner;

        @BindView(R.id.buyOrSellSpinner)
        ClickToSelectEditText buyOrSellSpinner;

        @BindView(R.id.stockInputEditText)
        EditText stockInputEditText;

        @BindView(R.id.stockInputLayout)
        TextInputLayout stockInputLayout;

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