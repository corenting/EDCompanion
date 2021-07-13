package fr.corenting.edcompanion.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommanderFleetFragment;
import fr.corenting.edcompanion.fragments.CommanderStatusFragment;
import fr.corenting.edcompanion.utils.CommanderUtils;

public class CommanderFragmentPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager fragmentManager;
    private final Context context;

    public CommanderFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentManager = fm;
        this.context = context;
    }

    @Override
    public int getCount() {
        return CommanderUtils.INSTANCE.hasFleetData(context) ? 2 : 1;
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
        if (position == 1) {
            return new CommanderFleetFragment();
        }
        return new CommanderStatusFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1) {
            return context.getString(R.string.fleet);
        }
        return context.getString(R.string.commander);
    }

    private String getTag(int position) {
        if (position == 1) {
            return CommanderFleetFragment.COMMANDER_FLEET_FRAGMENT_TAG;
        }
        return CommanderStatusFragment.COMMANDER_STATUS_FRAGMENT;
    }
}
