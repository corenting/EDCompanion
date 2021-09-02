package fr.corenting.edcompanion.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

import java.util.ArrayList

import fr.corenting.edcompanion.models.NameId
import fr.corenting.edcompanion.network.AutoCompleteNetwork

class AutoCompleteAdapter(private val context: Context, private val autocompleteType: Int) :
        BaseAdapter(), Filterable {
    private var resultList: List<String> = ArrayList()

    override fun getCount(): Int {
        return resultList.size
    }

    override fun getItem(index: Int): String {
        return resultList[index]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view:View? = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent,
                    false)
        }
        else {
            convertView
        }
        (view?.findViewById<View>(android.R.id.text1) as TextView).text = getItem(position)
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {

                    val results: List<String> = when (autocompleteType) {
                        TYPE_AUTOCOMPLETE_SYSTEMS -> AutoCompleteNetwork.searchSystems(context, constraint.toString())
                        TYPE_AUTOCOMPLETE_SHIPS -> AutoCompleteNetwork.searchShips(context, constraint.toString())
                        else -> AutoCompleteNetwork.searchCommodities(context, constraint.toString())
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = results
                    filterResults.count = results.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    val castedResults = (results.values as List<*>).filterIsInstance<String>()
                    if (castedResults.isNotEmpty()) {
                        resultList = castedResults
                    }
                }
                notifyDataSetInvalidated()
            }
        }
    }

    companion object {
        const val TYPE_AUTOCOMPLETE_SYSTEMS = 0
        const val TYPE_AUTOCOMPLETE_SHIPS = 1
        const val TYPE_AUTOCOMPLETE_COMMODITIES = 2
    }
}
