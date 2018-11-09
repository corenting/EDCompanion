package fr.corenting.edcompanion.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter<TViewHolder extends RecyclerView.ViewHolder, TItemType> extends RecyclerView.Adapter<TViewHolder> {

    protected List<TItemType> dataSet;
    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void removeAllItems() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setEnabled(false);
                int size = dataSet.size();
                dataSet.clear();
                notifyItemRangeRemoved(0, size);
                recyclerView.setEnabled(true);
            }
        });
    }

    public void addItems(final List<TItemType> items) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setEnabled(false);
                dataSet.addAll(items);
                notifyItemRangeInserted(0, items.size());
                recyclerView.setEnabled(true);
            }
        });
    }

    public void updateItem(final TItemType item, final int position) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setEnabled(false);
                dataSet.set(position, item);
                notifyItemChanged(position);
                recyclerView.setEnabled(true);
            }
        });
    }

    public List<TItemType> getItems() {
        return new ArrayList<>(dataSet);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
