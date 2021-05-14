package fr.corenting.edcompanion.adapters;

import android.content.Context;

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

    @Override
    public Fragment getItem(int position) {

        // Try to find existing fragment
        String tag = getTag(position);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            return fragment;
        }

        // Else return new one
        switch (position) {
            case 0:
                return new SystemDetailsFragment();
            case 1:
                return new SystemStationsFragment();
            case 2:
                return new SystemFactionsFragment();
            default:
                return new SystemDetailsFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.system);
            case 1:
                return context.getString(R.string.stations);
            case 2:
                return context.getString(R.string.factions);
            default:
                return context.getString(R.string.system);

        }
    }

    private String getTag(int position) {
        switch (position) {
            case 0:
                return SystemDetailsFragment.SYSTEM_DETAILS_FRAGMENT;
            case 1:
                return SystemStationsFragment.SYSTEM_STATIONS_FRAGMENT_TAG;
            case 2:
                return SystemFactionsFragment.SYSTEM_FACTIONS_FRAGMENT;
            default:
                return SystemDetailsFragment.SYSTEM_DETAILS_FRAGMENT;
        }
    }
}
