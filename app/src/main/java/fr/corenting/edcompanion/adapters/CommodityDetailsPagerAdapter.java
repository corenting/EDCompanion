package fr.corenting.edcompanion.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommodityDetailsBuyFragment;
import fr.corenting.edcompanion.fragments.CommodityDetailsFragment;
import fr.corenting.edcompanion.fragments.CommodityDetailsSellFragment;

public class CommodityDetailsPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager fragmentManager;
    private final Context context;

    public CommodityDetailsPagerAdapter(FragmentManager fm, Context context) {
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
            default:
                return new CommodityDetailsFragment();
            case 1:
                return new CommodityDetailsSellFragment();
            case 2:
                return new CommodityDetailsBuyFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
                return context.getString(R.string.commodity);
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
            case 1:
                return CommodityDetailsSellFragment.COMMODITY_DETAILS_SELL_FRAGMENT_TAG;
            case 2:
                return CommodityDetailsBuyFragment.COMMODITY_DETAILS_BUY_FRAGMENT_TAG;
        }
    }
}
