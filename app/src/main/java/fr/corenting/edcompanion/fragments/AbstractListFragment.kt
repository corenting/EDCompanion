package fr.corenting.edcompanion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import fr.corenting.edcompanion.R
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus

abstract class AbstractListFragment<TAdapter : androidx.recyclerview.widget.ListAdapter<*, *>> : Fragment() {

    protected lateinit var recyclerViewAdapter: TAdapter
    protected var loadDataOnCreate = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler view setup
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerViewAdapter = if (savedInstanceState == null) getNewRecyclerViewAdapter() else recyclerViewAdapter
        recyclerView.adapter = recyclerViewAdapter

        //Swipe to refresh setup
        val listener = {
            startLoading()
            getData()
        }
        swipeContainer.setOnRefreshListener(listener)
        emptySwipe.setOnRefreshListener(listener)

        // Load data if not restored
        if (savedInstanceState != null) {
            val count = recyclerViewAdapter.itemCount
            endLoading(count == 0)
        } else if (loadDataOnCreate) {
            startLoading()
            getData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onStart() {
        super.onStart()

        // Register eventbus for the list data
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    protected fun endLoading(empty: Boolean) {
        emptySwipe.visibility = if (empty) View.VISIBLE else View.GONE
        emptySwipe.isRefreshing = false
        swipeContainer.visibility = if (empty) View.GONE else View.VISIBLE
        swipeContainer.isRefreshing = false
    }

    private fun startLoading() {
        emptySwipe.visibility = View.GONE
        swipeContainer.visibility = View.VISIBLE
        swipeContainer.isRefreshing = true
    }


    internal abstract fun getData()
    internal abstract fun getNewRecyclerViewAdapter(): TAdapter
}
