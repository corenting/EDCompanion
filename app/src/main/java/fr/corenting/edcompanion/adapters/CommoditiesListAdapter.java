package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommoditiesListResult;
import fr.corenting.edcompanion.models.events.CommoditiesListSearch;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class CommoditiesListAdapter extends FinderAdapter<CommoditiesListAdapter.HeaderViewHolder,
        CommoditiesListAdapter.ResultViewHolder, CommoditiesListResult> {

    private final NumberFormat numberFormat;

    public CommoditiesListAdapter(Context context) {
        super(context);

        // Number format
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
        return R.layout.fragment_list_commodities_header;
    }

    @Override
    protected int getResultResId() {
        return R.layout.list_item_commodities_list_result;
    }

    @Override
    protected void bindHeaderViewHolder(final HeaderViewHolder holder) {
        // Commodity autocomplete
        holder.commodityInputEditText.setThreshold(3);
        holder.commodityInputEditText.setLoadingIndicator(holder.commodityProgressBar);
        holder.commodityInputEditText.setAdapter(new AutoCompleteAdapter(context,
                AutoCompleteAdapter.TYPE_AUTOCOMPLETE_COMMODITIES));
        holder.commodityInputEditText.setOnItemClickListener((adapterView, view, position, id) ->
                holder.commodityInputEditText.setText((String) adapterView.getItemAtPosition(position)));

        // Find button
        final Runnable onSubmit = () -> {
            // Don't launch search on empty strings or if find already in progress
            if (!findButtonEnabled || holder.commodityInputEditText.getText() != null &&
                    holder.commodityInputEditText.getText().length() == 0) {
                return;
            }

            ViewUtils.hideSoftKeyboard(holder.findButton.getRootView());
            CommoditiesListSearch result = new CommoditiesListSearch(
                    holder.commodityInputEditText.getText().toString());
            EventBus.getDefault().post(result);
        };
        holder.findButton.setOnClickListener(view -> onSubmit.run());
        holder.commodityInputEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                onSubmit.run();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void bindResultViewHolder(ResultViewHolder holder, int position) {
        final CommoditiesListResult currentResult = results.get(position - 1);

        holder.titleTextView.setText(currentResult.getName());
        holder.averagePriceTextView.setText(context.getString(R.string.credits,
                numberFormat.format(currentResult.getAveragePrice())));
        holder.isRareTextView.setVisibility(currentResult.isRare() ? View.VISIBLE : View.GONE);
        holder.categoryTextView.setText(currentResult.getCategory().getName());

        holder.itemLayout.setOnClickListener(v -> {
            /*Intent i = new Intent(context, SystemDetailsActivity.class);
            i.putExtra(context.getString(R.string.system), currentResult.getName());
            MiscUtils.startIntentWithFadeAnimation(context, i);*/
        });
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemLayout)
        RelativeLayout itemLayout;

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.categoryTextView)
        TextView categoryTextView;

        @BindView(R.id.averagePriceTextView)
        TextView averagePriceTextView;

        @BindView(R.id.isRareTextView)
        TextView isRareTextView;

        public ResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.commodityInputEditText)
        DelayAutoCompleteTextView commodityInputEditText;

        @BindView(R.id.commodityProgressBar)
        MaterialProgressBar commodityProgressBar;

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
