package fr.corenting.edcompanion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import fr.corenting.edcompanion.databinding.FragmentListBinding

abstract class AbstractListFragment<TAdapter : androidx.recyclerview.widget.ListAdapter<*, *>> :
    Fragment() {

    private var _binding: FragmentListBinding? = null
    protected val binding get() = _binding!!

    protected lateinit var recyclerViewAdapter: TAdapter
    protected var loadDataOnCreate = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler view setup
        val linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = linearLayoutManager
        recyclerViewAdapter = when {
            savedInstanceState == null || !this::recyclerViewAdapter.isInitialized -> getNewRecyclerViewAdapter()
            else -> recyclerViewAdapter
        }
        binding.recyclerView.adapter = recyclerViewAdapter

        //Swipe to refresh setup
        val listener = {
            startLoading()
            getData()
        }
        binding.swipeContainer.setOnRefreshListener(listener)
        binding.emptySwipe.setOnRefreshListener(listener)

        // Load data if not restored
        if (savedInstanceState != null) {
            val count = recyclerViewAdapter.itemCount
            endLoading(count == 0)
        } else if (loadDataOnCreate) {
            startLoading()
            getData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    protected fun endLoading(empty: Boolean) {
        binding.emptySwipe.visibility = if (empty) View.VISIBLE else View.GONE
        binding.emptySwipe.isRefreshing = false
        binding.swipeContainer.visibility = if (empty) View.GONE else View.VISIBLE
        binding.swipeContainer.isRefreshing = false
    }

    private fun startLoading() {
        binding.emptySwipe.visibility = View.GONE
        binding.swipeContainer.visibility = View.VISIBLE
        binding.swipeContainer.isRefreshing = true
    }

    internal abstract fun getData()
    internal abstract fun getNewRecyclerViewAdapter(): TAdapter
}
