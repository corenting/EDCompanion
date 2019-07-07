package fr.corenting.edcompanion.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommodityDetailsFragment;
import fr.corenting.edcompanion.fragments.SystemDetailsFragment;
import fr.corenting.edcompanion.fragments.SystemFactionsFragment;
import fr.corenting.edcompanion.fragments.SystemStationsFragment;

public class CommodityDetailsPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private Context context;

    public CommodityDetailsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
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
            default:
                return new CommodityDetailsFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
                return context.getString(R.string.informations);
            case 1:
                return context.getString(R.string.where_to_sell);
            case 2:
                return context.getString(R.string.where_to_buy);
        }
    }

    private String getTag(int position) {
        switch (position) {
            default:
                return CommodityDetailsFragment.COMMODITY_DETAILS_FRAGMENT;
        }
    }
}
