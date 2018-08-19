package fr.corenting.edcompanion.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class ListAdapter<TViewHolder extends RecyclerView.ViewHolder, TItemType> extends RecyclerView.Adapter<TViewHolder>  {

    protected List<TItemType> dataSet;

    public void removeAll() {
        int size = dataSet.size();
        dataSet.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void add(List<TItemType> items) {
        dataSet.addAll(items);
        notifyItemRangeInserted(0, items.size());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
