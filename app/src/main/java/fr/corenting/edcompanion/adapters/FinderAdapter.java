package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class FinderAdapter<THeaderViewHolder, TResultViewHolder, TDataType> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    protected final Context context;
    protected RecyclerView recyclerView;
    protected boolean findButtonEnabled;
    protected List<TDataType> results;

    public FinderAdapter(final Context context) {
        this.context = context;
        this.results = new ArrayList<>();
        this.findButtonEnabled = true;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return getHeaderViewHolder(parent);
        } else {
            return getResultViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeader(holder.getAdapterPosition())) {
            bindHeaderViewHolder((THeaderViewHolder) holder);
        } else {
            bindResultViewHolder((TResultViewHolder) holder, holder.getAdapterPosition());
        }
    }


    protected boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public void setFindButtonEnabled(boolean enabled) {
        findButtonEnabled = enabled;
    }

    @Override
    public int getItemCount() {
        if (results == null) {
            return 1;
        }
        return results.size() + 1;
    }

    public void setResults(final List<TDataType> newResults) {
        recyclerView.post(() -> {
            recyclerView.setEnabled(false);
            results = newResults;
            notifyItemRangeInserted(1, newResults.size());
            recyclerView.setEnabled(true);
        });
    }

    public void clearResults() {
        recyclerView.post(() -> {
            recyclerView.setEnabled(false);
            int size = results.size();
            results.clear();
            notifyItemRangeRemoved(1, size);
            recyclerView.setEnabled(true);
        });
    }

    protected abstract RecyclerView.ViewHolder getResultViewHolder(@NonNull ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent);

    protected abstract void bindResultViewHolder(TResultViewHolder holder, int adapterPosition);

    protected abstract void bindHeaderViewHolder(THeaderViewHolder holder);

}

