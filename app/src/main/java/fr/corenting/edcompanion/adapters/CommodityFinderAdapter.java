package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommodityFinderFragment;
import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.utils.SettingsUtils;
import fr.corenting.edcompanion.views.ClickToSelectEditText;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class CommodityFinderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private CommodityFinderFragment commodityFinderFragment;
    private List<CommodityFinderResult> results;
    private TextView emptyTextView;

    public CommodityFinderAdapter(final Context context,
                                  final CommodityFinderFragment commodityFinderFragment) {
        this.context = context;
        this.commodityFinderFragment = commodityFinderFragment;
        this.results = new LinkedList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_find_commodity_header,
                    parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commodity_finder_list_item,
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
            header.commodityInputEditText.setThreshold(3);
            header.commodityInputEditText.setLoadingIndicator(header.commodityProgressBar);
            header.commodityInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_COMMODITIES));
            header.commodityInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    header.commodityInputEditText.setText((String) adapterView.getItemAtPosition(position));
                }
            });

            // Landing pad size adapter
            if (header.landingPadSizeSpinner.getItems() == null || header.landingPadSizeSpinner.getItems().size() == 0)
            {
                String[] landingPadSizeArray = context.getResources().getStringArray(R.array.landing_pad_size);
                header.landingPadSizeSpinner.setItems(Arrays.asList(landingPadSizeArray));
            }

            // Find button
            header.findButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Convert stock value to int
                    String stockString = header.stockInputEditText.getText().toString();
                    int stock = 1;
                    if (stockString.length() != 0)
                    {
                        try
                        {
                            stock = Integer.parseInt(stockString);
                        }
                        catch (NumberFormatException e)
                        {
                            stock = 1;
                        }
                    }

                    commodityFinderFragment.onFindButtonClick((Button) view,
                            header.systemInputEditText.getText().toString(),
                            header.commodityInputEditText.getText().toString(),
                            header.stockInputEditText.getText().toString(),
                            stock);
                }
            });

            // Bind empty text view
            emptyTextView = header.emptyText;

        } else {
            CommodityFinderResult currentResult = results.get(position - 1);
            final ResultViewHolder resultViewHolder = (ResultViewHolder) holder;
            NumberFormat numberFormat = NumberFormat.getIntegerInstance(SettingsUtils.getUserLocale(context));

            resultViewHolder.titleTextView.setText(String.format("%s - %s", currentResult.System, currentResult.Station));
            resultViewHolder.landingPadTextView.setText(currentResult.LandingPad);
            resultViewHolder.priceTextView.setText(numberFormat.format(currentResult.BuyPrice));
            resultViewHolder.stockTextView.setText(numberFormat.format(currentResult.Stock));
            //TODO : add distance resultViewHolder.distanceTextView.setText(String.valueOf(currentResult.));
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

    public void setResults(List<CommodityFinderResult> newResults) {
        results.clear();
        results = newResults;
        notifyDataSetChanged();
    }

    public View getEmptyTextView() {
        return emptyTextView;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTextView)
        TextView titleTextView;

      /*  @BindView(R.id.distanceTextView)
        TextView distanceTextView;*/

        @BindView(R.id.stockTextView)
        TextView stockTextView;

        @BindView(R.id.priceTextView)
        TextView priceTextView;

        @BindView(R.id.landingPadTextView)
        TextView landingPadTextView;

        public ResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.systemInputEditText)
        DelayAutoCompleteTextView systemInputEditText;

        @BindView(R.id.commodityInputEditText)
        DelayAutoCompleteTextView commodityInputEditText;

        @BindView(R.id.landingPadSizeSpinner)
        ClickToSelectEditText landingPadSizeSpinner;

        @BindView(R.id.stockInputEditText)
        DelayAutoCompleteTextView stockInputEditText;

        @BindView(R.id.systemProgressBar)
        MaterialProgressBar systemProgressBar;

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
