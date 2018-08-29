package fr.corenting.edcompanion.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter<TViewHolder extends RecyclerView.ViewHolder, TItemType> extends RecyclerView.Adapter<TViewHolder>  {

    protected List<TItemType> dataSet;

    public void removeAllItems() {
        int size = dataSet.size();
        dataSet.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addItems(List<TItemType> items) {
        dataSet.addAll(items);
        notifyItemRangeInserted(0, items.size());
    }

    public void updateItem(TItemType item, int position) {
        dataSet.set(position, item);
        notifyItemChanged(position);
    }

    public List<TItemType> getItems() {
        return new ArrayList<>(dataSet);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
