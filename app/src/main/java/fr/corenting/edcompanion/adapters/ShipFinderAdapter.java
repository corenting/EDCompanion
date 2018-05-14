package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.ShipFinderFragment;
import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;

public class ShipFinderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private ShipFinderFragment shipFinderFragment;
    private List<ShipFinderResult> results;

    public ShipFinderAdapter(final Context context, final ShipFinderFragment shipFinderFragment) {
        this.context = context;
        this.shipFinderFragment = shipFinderFragment;
        this.results = new LinkedList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_find_ship_header,
                    parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ship_finder_list_item,
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
            header.systemInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS));
            header.systemInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    header.systemInputEditText.setText((String) adapterView.getItemAtPosition(position));
                }
            });

            // Ship autocomplete
            header.shipInputEditText.setThreshold(3);
            header.shipInputEditText.setAdapter(new AutoCompleteAdapter(context, AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SHIPS));
            header.shipInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    header.shipInputEditText.setText((String) adapterView.getItemAtPosition(position));
                }
            });

            // Find button
            header.findButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shipFinderFragment.onFindButtonClick((Button) view);
                }
            });

        } else {
            return;
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

    public void setResults(List<ShipFinderResult> results) {
        results.clear();
        this.results = results;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        public ResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.systemInputEditText)
        DelayAutoCompleteTextView systemInputEditText;

        @BindView(R.id.shipInputEditText)
        DelayAutoCompleteTextView shipInputEditText;

        @BindView(R.id.findButton)
        Button findButton;

        public HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
