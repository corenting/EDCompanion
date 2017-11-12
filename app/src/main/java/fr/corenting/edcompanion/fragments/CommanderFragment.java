package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommanderFragmentPagerAdapter;

public class CommanderFragment extends Fragment {

    public static final String COMMANDER_FRAGMENT = "commander_fragment";

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_commander, container, false);
        ButterKnife.bind(this, v);

        // Setup tablayout and viewpager
        viewPager.setAdapter(new CommanderFragmentPagerAdapter(getFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);

        // Style
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabTextColors(getResources().getColor(R.color.tabTextSelected), getResources().getColor(R.color.tabText));

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
