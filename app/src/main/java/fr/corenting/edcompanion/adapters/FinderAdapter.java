package fr.corenting.edcompanion.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class FinderAdapter<THeaderViewHolder, TResultViewHolder, TDataType> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    protected Context context;
    private RecyclerView recyclerView;
    protected boolean findButtonEnabled;
    protected List<TDataType> results;

    public FinderAdapter(final Context context) {
        this.context = context;
        this.results = new ArrayList<>();
        this.findButtonEnabled = true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(getHeaderResId(),
                    parent, false);
            return getNewHeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(getResultResId(),
                    parent, false);
            return getResultViewHolder(v);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    @SuppressWarnings("unchecked cast")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeader(holder.getAdapterPosition())) {
            bindHeaderViewHolder((THeaderViewHolder) holder);
        } else {
            bindResultViewHolder((TResultViewHolder) holder, holder.getAdapterPosition());
        }
    }

    private boolean isHeader(int position) {
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
        notifyItemChanged(0);
    }

    @Override
    public int getItemCount() {
        if (results == null)
        {
            return 1;
        }
        return results.size() + 1;
    }

    public void setResults(final List<TDataType> newResults) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setEnabled(false);
                results = newResults;
                notifyItemRangeInserted(1, newResults.size());
                recyclerView.setEnabled(true);
            }
        });
    }

    public void clearResults() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setEnabled(false);
                int size = results.size();
                results.clear();
                notifyItemRangeRemoved(1, size);
                recyclerView.setEnabled(true);
            }
        });
    }

    @SuppressWarnings("unchecked cast")
    protected THeaderViewHolder getHeader() {
        return (THeaderViewHolder) recyclerView.findViewHolderForAdapterPosition(0);
    }

    public abstract TextView getEmptyTextView();

    protected abstract RecyclerView.ViewHolder getNewHeaderViewHolder(View v);
    protected abstract RecyclerView.ViewHolder getResultViewHolder(View v);

    protected abstract int getHeaderResId();
    protected abstract int getResultResId();

    protected abstract void bindHeaderViewHolder(THeaderViewHolder holder);
    protected abstract void bindResultViewHolder(TResultViewHolder holder, int position);
}

