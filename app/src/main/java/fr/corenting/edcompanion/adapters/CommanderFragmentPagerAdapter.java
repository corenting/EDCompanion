package fr.corenting.edcompanion.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommanderFleetFragment;
import fr.corenting.edcompanion.fragments.CommanderStatusFragment;
import fr.corenting.edcompanion.network.player.PlayerNetwork;
import fr.corenting.edcompanion.utils.PlayerNetworkUtils;

public class CommanderFragmentPagerAdapter extends FragmentPagerAdapter {

    private final PlayerNetwork playerNetwork;
    private final FragmentManager fragmentManager;
    private final Context context;

    public CommanderFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentManager = fm;
        this.context = context;
        playerNetwork = PlayerNetworkUtils.getCurrentPlayerNetwork(context);
    }

    @Override
    public int getCount() {
        return playerNetwork.supportFleet() ? 2 : 1;
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
                return new CommanderStatusFragment();
            case 1:
                return new CommanderFleetFragment();
            default:
                return new CommanderStatusFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.commander);
            case 1:
                return context.getString(R.string.fleet);
            default:
                return context.getString(R.string.commander);

        }
    }

    private String getTag(int position) {
        switch (position) {
            case 0:
                return CommanderStatusFragment.COMMANDER_STATUS_FRAGMENT;
            case 1:
                return CommanderFleetFragment.COMMANDER_FLEET_FRAGMENT_TAG;
            default:
                return CommanderStatusFragment.COMMANDER_STATUS_FRAGMENT;
        }
    }
}
