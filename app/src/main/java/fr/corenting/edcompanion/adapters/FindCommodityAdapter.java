package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.NameId;
import fr.corenting.edcompanion.views.ClickToSelectEditText;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FindCommodityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<String> systems;
    private Context context;

    public FindCommodityAdapter(final Context context) {
        this.context = context;
        this.systems = new LinkedList<>();
    }

    public void addSystem(String system) {
        systems.add(system);
        notifyItemInserted(systems.size() - 1);
    }

    public void clearGoals() {
        systems.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_commodity_header, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new SystemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder header = (HeaderViewHolder) holder;

            // Include planetary adapter
            String[] includePlanetaryArray = context.getResources().getStringArray(R.array.yes_no_array);
            header.includePlanetarySpinner.setItems(Arrays.asList(includePlanetaryArray));
            header.includePlanetarySpinner.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<String>() {
                @Override
                public void onItemSelectedListener(String item, int selectedIndex) {

                }
            });

            // Buy or sell spinner adapter
            String[] buyOrSellArray = context.getResources().getStringArray(R.array.buy_or_sell_array);
            header.buyOrSellSpinner.setItems(Arrays.asList(buyOrSellArray));
            header.buyOrSellSpinner.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<String>() {
                @Override
                public void onItemSelectedListener(String item, int selectedIndex) {

                }
            });

            // Landing pad size adapter
            String[] landingPadSizeArray = context.getResources().getStringArray(R.array.landing_pad_array);
            header.landingPadSizeSpinner.setItems(Arrays.asList(landingPadSizeArray));
            header.landingPadSizeSpinner.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<String>() {
                @Override
                public void onItemSelectedListener(String item, int selectedIndex) {

                }
            });

            // System input
            header.systemInputEditText.setThreshold(3);
            header.systemInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS));
            header.systemInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    header.systemInputEditText.setText((String) adapterView.getItemAtPosition(position));
                }
            });

            // Commodities input
            header.commodityInputEditText.setThreshold(3);
            header.commodityInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_COMMODITIES));
            header.commodityInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    header.commodityInputEditText.setText((String) adapterView.getItemAtPosition(position));
                }
            });


        } else {
            String dataItem = getItem(position);
        }
    }

    @Override
    public int getItemCount() {
        return systems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private String getItem(int position) {
        return systems.get(position - 1);
    }

    public class SystemViewHolder extends RecyclerView.ViewHolder {

        public SystemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.commodityInputEditText)
        DelayAutoCompleteTextView commodityInputEditText;
        @BindView(R.id.systemInputEditText)
        DelayAutoCompleteTextView systemInputEditText;
        @BindView(R.id.buyOrSellSpinner)
        ClickToSelectEditText buyOrSellSpinner;
        @BindView(R.id.landingPadSizeSpinner)
        ClickToSelectEditText landingPadSizeSpinner;
        @BindView(R.id.includePlanetarySpinner)
        ClickToSelectEditText includePlanetarySpinner;
        @BindView(R.id.searchButton)
        Button searchButton;

        public HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
