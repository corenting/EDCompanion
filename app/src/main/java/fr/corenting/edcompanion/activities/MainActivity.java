package fr.corenting.edcompanion.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommanderFragment;
import fr.corenting.edcompanion.fragments.CommodityFinderFragment;
import fr.corenting.edcompanion.fragments.CommunityGoalsFragment;
import fr.corenting.edcompanion.fragments.DistanceCalculatorFragment;
import fr.corenting.edcompanion.fragments.GalnetFragment;
import fr.corenting.edcompanion.fragments.ShipFinderFragment;
import fr.corenting.edcompanion.models.ServerStatus;
import fr.corenting.edcompanion.network.ServerStatusNetwork;
import fr.corenting.edcompanion.utils.ChangelogUtils;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;
import fr.corenting.edcompanion.utils.ThemeUtils;
import fr.corenting.edcompanion.utils.ViewUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;
    @BindView(R.id.appBar)
    public AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set theme first before parent call
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getDarkThemeValue(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ThemeUtils.setToolbarColor(this, toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Setup navigation view and fake click the first item
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set initial fragment
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            switchFragment(CommunityGoalsFragment.COMMUNITY_GOALS_FRAGMENT_TAG);
            navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());
            setTitle(getString(R.string.community_goals));
            setActionBarSubtitle(R.string.inara_credits);
        }

        // Update the server status
        updateServerStatus();

        // Set listener on server status text to refresh it
        TextView drawerSubtitleTextView = navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
        drawerSubtitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateServerStatus();
            }
        });

        // Push notifications setup
        NotificationsUtils.refreshPushSubscriptions(this);

        // Show changelog
        ChangelogUtils.ShowChangelog(this);

        // Init ThreeTen
        AndroidThreeTen.init(getApplicationContext());
    }

    private void clearActionBarSubtitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("");
        }
    }

    private void setActionBarSubtitle(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(getString(resId));
        }
    }

    private void updateServerStatus() {
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
        textView.setText(getString(R.string.updating_server_status));
        ServerStatusNetwork.getStatus(this);
    }

    @Subscribe
    public void onServerStatusEvent(ServerStatus status) {
        String content = status.Success ? status.Status : getString(R.string.unknown);
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
        textView.setText(getString(R.string.server_status, content));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        expandToolbar();
        setTitle(item.getTitle());
        clearActionBarSubtitle();
        switch (item.getItemId()) {
            case R.id.nav_cg:
                switchFragment(CommunityGoalsFragment.COMMUNITY_GOALS_FRAGMENT_TAG);
                setActionBarSubtitle(R.string.inara_credits);
                break;
            case R.id.nav_cmdr:
                switchFragment(CommanderFragment.COMMANDER_FRAGMENT);
                String commanderName = SettingsUtils.getCommanderName(this);
                setTitle(commanderName.equals("") ? getString(R.string.commander) : commanderName);
                break;
            case R.id.nav_galnet_news: {
                switchFragment(GalnetFragment.GALNET_FRAGMENT_TAG);
                break;
            }
            case R.id.nav_galnet_reports: {
                switchFragment(GalnetFragment.GALNET_REPORTS_FRAGMENT_TAG);
                break;
            }
            case R.id.nav_distance_calculator: {
                switchFragment(DistanceCalculatorFragment.DISTANCE_CALCULATOR_FRAGMENT_TAG);
                setActionBarSubtitle(R.string.eddb_credits);
                break;
            }
            case R.id.nav_commodity_finder: {
                switchFragment(CommodityFinderFragment.COMMODITY_FINDER_FRAGMENT_TAG);
                setActionBarSubtitle(R.string.edm_credits);
                break;
            }
            case R.id.nav_ship_finder: {
                switchFragment(ShipFinderFragment.SHIP_FINDER_FRAGMENT_TAG);
                setActionBarSubtitle(R.string.eddb_credits);
                break;
            }
            case R.id.nav_about: {
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
            case R.id.nav_settings: {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        Bundle args = new Bundle();
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment, tag).commit();
        }
        switch (tag) {
            case GalnetFragment.GALNET_FRAGMENT_TAG:
                fragment = new GalnetFragment();
                args.putBoolean("reportsMode", false);
                fragment.setArguments(args);
                ViewUtils.switchFragment(fragmentManager, fragment, tag);
                break;
            case GalnetFragment.GALNET_REPORTS_FRAGMENT_TAG:
                fragment = new GalnetFragment();
                args.putBoolean("reportsMode", true);
                fragment.setArguments(args);
                ViewUtils.switchFragment(fragmentManager, fragment, tag);
                break;
            case CommanderFragment.COMMANDER_FRAGMENT:
                ViewUtils.switchFragment(fragmentManager, new CommanderFragment(), tag);
                break;
            case DistanceCalculatorFragment.DISTANCE_CALCULATOR_FRAGMENT_TAG:
                ViewUtils.switchFragment(fragmentManager, new DistanceCalculatorFragment(), tag);
                break;
            case ShipFinderFragment.SHIP_FINDER_FRAGMENT_TAG:
                ViewUtils.switchFragment(fragmentManager, new ShipFinderFragment(), tag);
                break;
            case CommodityFinderFragment.COMMODITY_FINDER_FRAGMENT_TAG:
                ViewUtils.switchFragment(fragmentManager, new CommodityFinderFragment(), tag);
                break;
            default:
                ViewUtils.switchFragment(fragmentManager, new CommunityGoalsFragment(), tag);
                break;
        }

        // Also set toolbar elevation to prevent shadow with tabLayout
        if (tag.equals(CommanderFragment.COMMANDER_FRAGMENT)) {
            setToolbarElevation(0);
        } else {
            setToolbarElevation(ViewUtils.dpToPx(this, 4));
        }
    }

    private void expandToolbar() {
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        if (appBarLayout != null) {
            appBarLayout.setExpanded(true);
        }
    }

    private void setToolbarElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setStateListAnimator(null);
        }
        ViewCompat.setElevation(appBarLayout, elevation);
    }
}