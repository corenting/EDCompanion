package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.CommodityDetailsActivity;
import fr.corenting.edcompanion.databinding.FragmentListCommoditiesHeaderBinding;
import fr.corenting.edcompanion.databinding.ListItemCommoditiesListResultBinding;
import fr.corenting.edcompanion.models.CommoditiesListResult;
import fr.corenting.edcompanion.models.events.CommoditiesListSearch;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.utils.MiscUtils;
import fr.corenting.edcompanion.utils.ViewUtils;

public class CommoditiesListAdapter extends FinderAdapter<CommoditiesListAdapter.HeaderViewHolder,
        CommoditiesListAdapter.ResultViewHolder, CommoditiesListResult> {

    private final NumberFormat numberFormat;

    public CommoditiesListAdapter(Context context) {
        super(context);

        // Number format
        numberFormat = MathUtils.getNumberFormat(context);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull @NotNull ViewGroup parent) {
        FragmentListCommoditiesHeaderBinding headerBinding = FragmentListCommoditiesHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HeaderViewHolder(headerBinding);
    }

    @Override
    protected RecyclerView.ViewHolder getResultViewHolder(@NonNull @NotNull ViewGroup parent) {
        ListItemCommoditiesListResultBinding resultBinding = ListItemCommoditiesListResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ResultViewHolder(resultBinding);
    }

    @Override
    protected void bindHeaderViewHolder(final HeaderViewHolder holder) {
        // Commodity autocomplete
        holder.binding.commodityInputEditText.setThreshold(3);
        holder.binding.commodityInputEditText.setAdapter(new AutoCompleteAdapter(context,
                AutoCompleteAdapter.TYPE_AUTOCOMPLETE_COMMODITIES));
        holder.binding.commodityInputEditText.setOnItemClickListener((adapterView, view, position, id) ->
                holder.binding.commodityInputEditText.setText((String) adapterView.getItemAtPosition(position)));

        // Find button
        final Runnable onSubmit = () -> {
            // Don't launch search on empty strings or if find already in progress
            if (!findButtonEnabled || holder.binding.commodityInputEditText.getText() != null &&
                    holder.binding.commodityInputEditText.getText().length() == 0) {
                return;
            }

            ViewUtils.hideSoftKeyboard(holder.binding.findButton.getRootView());
            CommoditiesListSearch result = new CommoditiesListSearch(
                    holder.binding.commodityInputEditText.getText().toString());
            EventBus.getDefault().post(result);
        };
        holder.binding.findButton.setOnClickListener(view -> onSubmit.run());
        holder.binding.commodityInputEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
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

        holder.binding.titleTextView.setText(currentResult.getName());
        holder.binding.averagePriceTextView.setText(context.getString(R.string.credits,
                numberFormat.format(currentResult.getAveragePrice())));
        holder.binding.isRareTextView.setVisibility(currentResult.isRare() ? View.VISIBLE : View.GONE);
        holder.binding.categoryTextView.setText(currentResult.getCategory().getName());

        holder.binding.itemLayout.setOnClickListener(v -> {
            Intent i = new Intent(context, CommodityDetailsActivity.class);
            i.putExtra("data", currentResult.getName());
            MiscUtils.startIntentWithFadeAnimation(context, i);
        });
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        private final ListItemCommoditiesListResultBinding binding;

        public ResultViewHolder(ListItemCommoditiesListResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final FragmentListCommoditiesHeaderBinding binding;

        public HeaderViewHolder(FragmentListCommoditiesHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
