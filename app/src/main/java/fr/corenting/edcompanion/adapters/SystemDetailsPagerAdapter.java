package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommanderFleetFragment;
import fr.corenting.edcompanion.fragments.CommanderStatusFragment;
import fr.corenting.edcompanion.fragments.SystemDetailsFragment;

public class SystemDetailsPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private Context context;

    public SystemDetailsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
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
                return new CommanderFleetFragment();
            default:
                return new SystemDetailsFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.system_label);
            case 1:
                return context.getString(R.string.stations);
            default:
                return context.getString(R.string.system_label);

        }
    }

    private String getTag(int position) {
        switch (position) {
            case 0:
                return SystemDetailsFragment.SYSTEM_DETAILS_FRAGMENT;
            case 1:
                return CommanderFleetFragment.COMMANDER_FLEET_FRAGMENT_TAG;
            default:
                return SystemDetailsFragment.SYSTEM_DETAILS_FRAGMENT;
        }
    }
}
