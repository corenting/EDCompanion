package fr.corenting.edcompanion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommunityGoalsFragment;
import fr.corenting.edcompanion.fragments.FindCommodityFragment;
import fr.corenting.edcompanion.fragments.GalnetFragment;
import fr.corenting.edcompanion.fragments.StatusFragment;
import fr.corenting.edcompanion.network.ServerStatusNetwork;
import fr.corenting.edcompanion.utils.SettingsUtils;
import fr.corenting.edcompanion.utils.ThemeUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set theme first before parent call
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getDarkThemeValue(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ThemeUtils.setToolbarColor(this, toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Enable Ion logging in debug
        if (BuildConfig.DEBUG) {
            Ion.getDefault(getApplicationContext()).configure().setLogging("EDCompanion networking", Log.DEBUG);
        }

        // Set user agent to name + version of the app
        Ion.getDefault(getApplicationContext()).configure().userAgent(String.format(getString(R.string.user_agent), BuildConfig.VERSION_NAME, System.getProperty("http.agent")));

        // Setup navigation view and fake click the first item
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set initial fragment
        fragmentManager = getSupportFragmentManager();
        switchFragment(CommunityGoalsFragment.COMMUNITY_GOALS_FRAGMENT_TAG);

        // Select the first item in menu as the fragment was loaded
        navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());
        setTitle(getString(R.string.community_goals));
        getSupportActionBar().setSubtitle(R.string.inara_credits);

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
    }

    private void updateServerStatus() {
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
        textView.setText(getString(R.string.updating_server_status));
        ServerStatusNetwork.getStatus(this);
    }

    @Subscribe
    public void onServerStatusEvent(String status) {
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
        textView.setText(getString(R.string.server_status, status));
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
        int id = item.getItemId();

        expandToolbar();
        switch (id) {
            case R.id.nav_cg:
                switchFragment(CommunityGoalsFragment.COMMUNITY_GOALS_FRAGMENT_TAG);
                setTitle(getString(R.string.community_goals));
                getSupportActionBar().setSubtitle(R.string.inara_credits);
                break;
            case R.id.nav_cmdr:
                switchFragment(StatusFragment.STATUS_FRAGMENT_TAG);
                String commanderName = SettingsUtils.getCommanderName(this);
                setTitle(commanderName.equals("") ? getString(R.string.status) : commanderName);
                getSupportActionBar().setSubtitle("");
                break;
            case R.id.nav_galnet_news: {
                switchFragment(GalnetFragment.GALNET_FRAGMENT_TAG);
                setTitle(getString(R.string.galnet));
                getSupportActionBar().setSubtitle("");
                break;
            }
            case R.id.nav_galnet_reports: {
                switchFragment(GalnetFragment.GALNET_REPORTS_FRAGMENT_TAG);
                setTitle(getString(R.string.galnet_reports));
                getSupportActionBar().setSubtitle("");
                break;
            }
          /*  case R.id.nav_find_commodity: {
                switchFragment(FindCommodityFragment.FIND_COMMODITY_FRAGMENT_TAG);
                setTitle(getString(R.string.find_commodity));
                getSupportActionBar().setSubtitle(R.string.eddb_credits);
                break;
            }*/
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
                // Change fragment
                fragmentManager
                        .beginTransaction().replace(R.id.fragmentContent, fragment, GalnetFragment.GALNET_FRAGMENT_TAG)
                        .commit();
                break;
            case GalnetFragment.GALNET_REPORTS_FRAGMENT_TAG:
                fragment = new GalnetFragment();
                args.putBoolean("reportsMode", true);
                fragment.setArguments(args);
                // Change fragment
                fragmentManager
                        .beginTransaction().replace(R.id.fragmentContent, fragment, GalnetFragment.GALNET_REPORTS_FRAGMENT_TAG)
                        .commit();
                break;
            case StatusFragment.STATUS_FRAGMENT_TAG:
                fragmentManager
                        .beginTransaction().replace(R.id.fragmentContent, new StatusFragment(), StatusFragment.STATUS_FRAGMENT_TAG)
                        .commit();
                break;
            case FindCommodityFragment.FIND_COMMODITY_FRAGMENT_TAG:
                fragmentManager
                        .beginTransaction().replace(R.id.fragmentContent, new FindCommodityFragment(), FindCommodityFragment.FIND_COMMODITY_FRAGMENT_TAG)
                        .commit();
                break;
            default:
                fragmentManager.beginTransaction().replace(R.id.fragmentContent,
                        new CommunityGoalsFragment(),
                        CommunityGoalsFragment.COMMUNITY_GOALS_FRAGMENT_TAG).commit();
                break;
        }
    }

    private void expandToolbar() {
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        if (appBarLayout != null) {
            appBarLayout.setExpanded(true);
        }
    }
}
