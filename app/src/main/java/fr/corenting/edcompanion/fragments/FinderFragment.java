package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.itemanimators.SlideInOutLeftAnimator;
import com.mikepenz.itemanimators.SlideInOutRightAnimator;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.FinderAdapter;

public abstract class FinderFragment<TAdapter extends FinderAdapter> extends Fragment {

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    protected TAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finder, container, false);
        ButterKnife.bind(this, v);

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = getNewRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new SlideInOutLeftAnimator(recyclerView));

        // Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeToRefresh();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);

        return v;
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

    protected void endLoading(boolean isEmpty) {
        recyclerViewAdapter.getEmptyTextView().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void startLoading() {
        recyclerViewAdapter.clearResults();
        swipeRefreshLayout.setRefreshing(true);
        recyclerViewAdapter.getEmptyTextView().setVisibility(View.GONE);
    }

    public abstract TAdapter getNewRecyclerViewAdapter();

    public abstract void onSwipeToRefresh();
}