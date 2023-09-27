package fr.corenting.edcompanion.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommanderFleetFragment;
import fr.corenting.edcompanion.fragments.CommanderLoadoutsFragment;
import fr.corenting.edcompanion.fragments.CommanderStatusFragment;
import fr.corenting.edcompanion.utils.CommanderUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;

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
        boolean displayFleet = SettingsUtils.getBoolean(context, context.getString(R.string.settings_cmdr_loadout_display_enable), true) && CommanderUtils.INSTANCE.hasLoadoutData(context);
        return 1 + (CommanderUtils.INSTANCE.hasFleetData(context) ? 1 : 0) + (displayFleet ? 1 : 0);
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
            case 1 -> new CommanderFleetFragment();
            case 2 -> new CommanderLoadoutsFragment();
            default -> new CommanderStatusFragment();
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case 1 -> context.getString(R.string.fleet);
            case 2 -> context.getString(R.string.loadouts);
            default -> context.getString(R.string.commander);
        };
    }

    private String getTag(int position) {
        return switch (position) {
            case 1 -> CommanderFleetFragment.COMMANDER_FLEET_FRAGMENT_TAG;
            case 2 -> CommanderLoadoutsFragment.COMMANDER_LOADOUTS_FRAGMENT_TAG;
            default -> CommanderStatusFragment.COMMANDER_STATUS_FRAGMENT;
        };
    }
}
