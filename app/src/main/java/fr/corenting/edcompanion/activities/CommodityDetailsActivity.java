package fr.corenting.edcompanion.activities;

import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import fr.corenting.edcompanion.adapters.CommodityDetailsPagerAdapter;

public class CommodityDetailsActivity extends AbstractViewPagerActivity {

    @NotNull
    @Override
    public String getDefaultData() {
        return "Cobalt";
    }

    @NotNull
    @Override
    public FragmentPagerAdapter getPagerAdapter() {
        return new CommodityDetailsPagerAdapter(getSupportFragmentManager(), this);
    }

    @Override
    public void getData() {
        // TODO
    }
}
