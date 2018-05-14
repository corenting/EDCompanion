package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.NameId;
import fr.corenting.edcompanion.network.AutoCompleteNetwork;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

    public static int TYPE_AUTOCOMPLETE_SYSTEMS = 0;
    public static int TYPE_AUTOCOMPLETE_SHIPS = 0;

    private Context context;
    private List<NameId> resultList = new ArrayList<>();
    private int autocompleteType;

    public AutoCompleteAdapter(Context context, int type) {
        this.context = context;
        this.autocompleteType = type;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index).Name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    List<NameId> results;
                    if (autocompleteType == TYPE_AUTOCOMPLETE_SYSTEMS) {
                        results = AutoCompleteNetwork.searchSystems(context, constraint.toString());
                    }
                    else
                    {
                        results = AutoCompleteNetwork.searchShips(context, constraint.toString());
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = results;
                    filterResults.count = results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<NameId>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
    }
}
