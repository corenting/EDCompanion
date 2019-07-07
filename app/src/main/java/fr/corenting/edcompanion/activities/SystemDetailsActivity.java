package fr.corenting.edcompanion.activities;

import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import fr.corenting.edcompanion.adapters.SystemDetailsPagerAdapter;
import fr.corenting.edcompanion.network.SystemNetwork;

public class SystemDetailsActivity extends AbstractViewPagerActivity {

    @NotNull
    @Override
    public String getDefaultData() {
        return "Sol";
    }

    @NotNull
    @Override
    public FragmentPagerAdapter getPagerAdapter() {
        return new SystemDetailsPagerAdapter(getSupportFragmentManager(), this);
    }

    @Override
    public void getData() {
        SystemNetwork.getSystemDetails(this, dataName);
        SystemNetwork.getSystemHistory(this, dataName);
        SystemNetwork.getSystemStations(this, dataName);
    }
}
