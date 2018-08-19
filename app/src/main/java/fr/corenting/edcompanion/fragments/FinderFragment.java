package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.FinderAdapter;
import fr.corenting.edcompanion.models.BaseModel;
import fr.corenting.edcompanion.models.ResultsList;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public abstract class FinderFragment<TAdapter extends FinderAdapter> extends Fragment {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    public MaterialProgressBar progressBar;

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
        recyclerView.setItemAnimator(new SlideInLeftAnimator());

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
        progressBar.setVisibility(View.GONE);
    }

    protected void startLoading() {
        recyclerViewAdapter.clearResults();
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewAdapter.getEmptyTextView().setVisibility(View.GONE);
    }

    public abstract TAdapter getNewRecyclerViewAdapter();
}