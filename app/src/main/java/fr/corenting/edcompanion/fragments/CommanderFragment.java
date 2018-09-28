package fr.corenting.edcompanion.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommanderFragmentPagerAdapter;
import fr.corenting.edcompanion.utils.ThemeUtils;

public class CommanderFragment extends Fragment {

    public static final String COMMANDER_FRAGMENT = "commander_fragment";

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_commander, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup tablayout and viewpager
        viewPager.setAdapter(new CommanderFragmentPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);

        // Style
        if (ThemeUtils.isDarkThemeEnabled(getContext())) {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.darkPrimary));
        } else {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        tabLayout.setTabTextColors(getResources().getColor(R.color.tabTextSelected),
                getResources().getColor(R.color.tabText));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
