package fr.corenting.edcompanion.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.FinderAdapter;
import fr.corenting.edcompanion.databinding.FragmentFinderBinding;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public abstract class AbstractFinderFragment<TAdapter extends FinderAdapter> extends Fragment {

    private FragmentFinderBinding binding;

    protected TAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFinderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = savedInstanceState == null ?
                getNewRecyclerViewAdapter() : recyclerViewAdapter;
        binding.recyclerView.setAdapter(recyclerViewAdapter);

        // Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = this::onSwipeToRefresh;
        binding.swipeContainer.setOnRefreshListener(listener);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void endLoading(boolean isEmpty) {
        recyclerViewAdapter.setFindButtonEnabled(true);
        binding.swipeContainer.setRefreshing(false);

        Activity activity = getActivity();
        if (activity != null && isEmpty) {
            NotificationsUtils.displaySnackbar(getActivity(),
                    getString(R.string.no_results));
        }
    }

    protected void startLoading() {
        recyclerViewAdapter.setFindButtonEnabled(false);
        recyclerViewAdapter.clearResults();
        binding.swipeContainer.setRefreshing(true);
    }

    public abstract TAdapter getNewRecyclerViewAdapter();

    public abstract void onSwipeToRefresh();

}