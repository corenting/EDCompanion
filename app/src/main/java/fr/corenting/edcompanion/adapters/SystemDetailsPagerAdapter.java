package fr.corenting.edcompanion.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.SystemDetailsFragment;
import fr.corenting.edcompanion.fragments.SystemFactionsFragment;
import fr.corenting.edcompanion.fragments.SystemStationsFragment;

public class SystemDetailsPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager fragmentManager;
    private final Context context;

    public SystemDetailsPagerAdapter(FragmentManager fm, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        // Try to find existing fragment
        String tag = getTag(position);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            return fragment;
        }

        // Else return new one
        return switch (position) {
            case 1 -> new SystemStationsFragment();
            case 2 -> new SystemFactionsFragment();
            default -> new SystemDetailsFragment();
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case 1 -> context.getString(R.string.stations);
            case 2 -> context.getString(R.string.factions);
            default -> context.getString(R.string.system);
        };
    }

    private String getTag(int position) {
        return switch (position) {
            case 1 -> SystemStationsFragment.SYSTEM_STATIONS_FRAGMENT_TAG;
            case 2 -> SystemFactionsFragment.SYSTEM_FACTIONS_FRAGMENT;
            default -> SystemDetailsFragment.SYSTEM_DETAILS_FRAGMENT;
        };
    }
}
