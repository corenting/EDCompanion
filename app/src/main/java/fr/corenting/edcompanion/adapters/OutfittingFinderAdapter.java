package fr.corenting.edcompanion.adapters;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Instant;

import java.text.NumberFormat;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.databinding.FragmentFindOutfittingHeaderBinding;
import fr.corenting.edcompanion.databinding.ListStationFinderResultBinding;
import fr.corenting.edcompanion.models.OutfittingFinderResult;
import fr.corenting.edcompanion.models.events.OutfittingFinderSearch;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.utils.StationTypeUtils;
import fr.corenting.edcompanion.utils.ViewUtils;

public class OutfittingFinderAdapter extends FinderAdapter<OutfittingFinderAdapter.HeaderViewHolder, OutfittingFinderAdapter.ResultViewHolder, OutfittingFinderResult> {

    private final NumberFormat numberFormat;

    public OutfittingFinderAdapter(Context context) {
        super(context);
        numberFormat = MathUtils.getNumberFormat(context);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NotNull ViewGroup parent) {
        FragmentFindOutfittingHeaderBinding headerBinding = FragmentFindOutfittingHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HeaderViewHolder(headerBinding);
    }

    @Override
    protected RecyclerView.ViewHolder getResultViewHolder(@NonNull @NotNull ViewGroup parent) {
        ListStationFinderResultBinding resultBinding = ListStationFinderResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ResultViewHolder(resultBinding);
    }

    @Override
    protected void bindHeaderViewHolder(final HeaderViewHolder holder) {
        // Outfitting autocomplete
        holder.binding.outfittingInputEditText.setThreshold(3);
        holder.binding.outfittingInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_OUTFITTINGS));
        holder.binding.outfittingInputEditText.setOnItemClickListener((adapterView, view, position, id) ->
                holder.binding.outfittingInputEditText.setText((String) adapterView.getItemAtPosition(position)));

        // Landing pad size adapter
        if (holder.binding.landingPadSizeAutoCompleteTextView.getAdapter() == null ||
                holder.binding.landingPadSizeAutoCompleteTextView.getAdapter().getCount() == 0) {
            String[] landingPadSizeArray = context.getResources()
                    .getStringArray(R.array.landing_pad_size);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_dropdown_item_1line, landingPadSizeArray);
            holder.binding.landingPadSizeAutoCompleteTextView.setText(landingPadSizeArray[0]);
            holder.binding.landingPadSizeAutoCompleteTextView.setAdapter(adapter);
        }

        // Find button
        final Runnable onSubmit = () -> {
            if (!findButtonEnabled) {
                return;
            }

            ViewUtils.hideSoftKeyboard(holder.binding.findButton.getRootView());

            OutfittingFinderSearch result = new OutfittingFinderSearch(
                    holder.binding.outfittingInputEditText.getText().toString(),
                    holder.binding.systemInputView.getText().toString(),
                    holder.binding.landingPadSizeAutoCompleteTextView.getText().toString());

            EventBus.getDefault().post(result);
        };

        // On submit stuff
        holder.binding.findButton.setOnClickListener(view -> onSubmit.run());
        holder.binding.outfittingInputEditText.setOnSubmit(onSubmit);
        holder.binding.systemInputView.setOnSubmit(onSubmit);
    }

    @Override
    protected void bindResultViewHolder(ResultViewHolder holder, int position) {
        OutfittingFinderResult currentResult = results.get(position - 1);

        // Hide unused fields
        holder.binding.stockLabelTextView.setVisibility(View.GONE);
        holder.binding.stockTextView.setVisibility(View.GONE);
        holder.binding.priceLabelTextView.setVisibility(View.GONE);
        holder.binding.priceTextView.setVisibility(View.GONE);

        // Update date
        String date = android.text.format.DateUtils.getRelativeTimeSpanString(
                currentResult.getLastOutfittingUpdate().toEpochMilli(), Instant.now().toEpochMilli(),
                0, FORMAT_ABBREV_RELATIVE).toString();
        holder.binding.lastUpdateTextView.setText(date);

        // Display is special type (fleet carrier, settlement etc...)
        String stationType = StationTypeUtils.INSTANCE.getStationTypeText(
                context,
                currentResult.isPlanetary(),
                currentResult.isFleetCarrier(),
                currentResult.isSettlement()
        );
        holder.binding.stationTypeTextView.setText(stationType);
        holder.binding.stationTypeTextView.setVisibility(stationType != null ? View.VISIBLE : View.GONE);

        // Other information
        holder.binding.titleTextView.setText(String.format("%s - %s", currentResult.getSystem(),
                currentResult.getStation()));
        holder.binding.isPlanetaryImageView.setVisibility(
                currentResult.isPlanetary() ? View.VISIBLE : View.GONE);
        holder.binding.landingPadTextView.setText(currentResult.getLandingPad());
        holder.binding.distanceTextView.setText(context.getString(R.string.distance_ly,
                currentResult.getDistanceFromReferenceSystem()));
        holder.binding.distanceToStarTextView.setText(context.getString(R.string.distance_ls, numberFormat
                .format(currentResult.getDistanceToArrival())));

    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        private final ListStationFinderResultBinding binding;

        public ResultViewHolder(ListStationFinderResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final FragmentFindOutfittingHeaderBinding binding;

        public HeaderViewHolder(FragmentFindOutfittingHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}