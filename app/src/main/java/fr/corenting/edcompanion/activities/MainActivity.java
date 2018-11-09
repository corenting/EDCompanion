package fr.corenting.edcompanion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommanderFragment;
import fr.corenting.edcompanion.fragments.CommodityFinderFragment;
import fr.corenting.edcompanion.fragments.CommunityGoalsFragment;
import fr.corenting.edcompanion.fragments.DistanceCalculatorFragment;
import fr.corenting.edcompanion.fragments.GalnetFragment;
import fr.corenting.edcompanion.fragments.ShipFinderFragment;
import fr.corenting.edcompanion.fragments.SystemFinderFragment;
import fr.corenting.edcompanion.models.events.ServerStatus;
import fr.corenting.edcompanion.network.ServerStatusNetwork;
import fr.corenting.edcompanion.utils.ChangelogUtils;
import fr.corenting.edcompanion.utils.HideKeyboardDrawerListener;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;
import fr.corenting.edcompanion.utils.ThemeUtils;
import fr.corenting.edcompanion.utils.ViewUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String KEY_CURRENT_TITLE = "CURRENT_TITLE";
    private String KEY_CURRENT_SUBTITLE = "CURRENT_SUBTITLE";
    private String KEY_CURRENT_TAG = "CURRENT_TAG";

    @BindView(R.id.drawerLayout)
    public DrawerLayout drawer;
    @BindView(R.id.navView)
    public NavigationView navigationView;
    @BindView(R.id.appBar)
    public AppBarLayout appBarLayout;

    private FragmentManager fragmentManager;
    private CharSequence currentTitle;
    private CharSequence currentSubtitle;
    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new HideKeyboardDrawerListener(drawer.getRootView()));
        toggle.syncState();

        // Setup navigation view and fake click the first item
        navigationView.setNavigationItemSelectedListener(this);

        // Set initial fragment
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());
            switchOnNavigation(getString(R.string.community_goals), R.id.nav_cg);
        } else {
            currentTitle = savedInstanceState.getCharSequence(KEY_CURRENT_TITLE);
            currentSubtitle = savedInstanceState.getCharSequence(KEY_CURRENT_SUBTITLE);
            currentFragmentTag = savedInstanceState.getString(KEY_CURRENT_TAG);
            Fragment fragment = fragmentManager.findFragmentByTag(currentFragmentTag);
            ViewUtils.switchFragment(fragmentManager, fragment, currentFragmentTag);
            updateActionBar();
        }

        // Update the server status
        updateServerStatus();

        // Set listener on server status text to refresh it, and set theme
        TextView drawerSubtitleTextView = navigationView.getHeaderView(0)
                .findViewById(R.id.drawerSubtitleTextView);
        if (ThemeUtils.isDarkThemeEnabled(this)) {
            LinearLayout drawerHeader = navigationView.getHeaderView(0)
                    .findViewById(R.id.headerLinearLayout);
            drawerHeader.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.darkPrimary));
        }
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(KEY_CURRENT_TITLE, currentTitle);
        outState.putCharSequence(KEY_CURRENT_SUBTITLE, currentSubtitle);
        outState.putString(KEY_CURRENT_TAG, currentFragmentTag);
        super.onSaveInstanceState(outState);
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
        drawer.closeDrawer(GravityCompat.START);
        return switchOnNavigation(item.getTitle().toString(), item.getItemId());
    }

    @Override
    protected void onResume() {
        updateActionBar();
        super.onResume();
    }

    private void updateActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(currentSubtitle);
            setTitle(currentTitle);
        }
        fixToolbarElevation();
    }

    private void updateServerStatus() {
        TextView textView = navigationView.getHeaderView(0)
                .findViewById(R.id.drawerSubtitleTextView);
        textView.setText(getString(R.string.updating_server_status));
        ServerStatusNetwork.getStatus(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerStatusEvent(ServerStatus status) {
        String content = status.getSuccess() ? status.getStatus() : getString(R.string.unknown);
        TextView textView = navigationView.getHeaderView(0)
                .findViewById(R.id.drawerSubtitleTextView);
        textView.setText(getString(R.string.server_status, content));
    }

    private boolean switchOnNavigation(String title, int id) {
        expandToolbar();

        // Set title and subtitle default values
        if (id != R.id.nav_settings && id != R.id.nav_about) {
            currentTitle = title;
            currentSubtitle = "";
        }

        switch (id) {
            case R.id.nav_cg:
                switchFragment(CommunityGoalsFragment.COMMUNITY_GOALS_FRAGMENT_TAG);
                currentSubtitle = getString(R.string.inara_credits);
                break;
            case R.id.nav_cmdr:
                switchFragment(CommanderFragment.COMMANDER_FRAGMENT);
                String commanderName = SettingsUtils.getCommanderName(this);
                currentTitle = commanderName.equals("") ?
                        getString(R.string.commander) : commanderName;
                break;
            case R.id.nav_galnet_news: {
                switchFragment(GalnetFragment.GALNET_FRAGMENT_TAG);
                break;
            }
            case R.id.nav_galnet_reports: {
                switchFragment(GalnetFragment.GALNET_REPORTS_FRAGMENT_TAG);
                break;
            }
            case R.id.nav_systems: {
                switchFragment(SystemFinderFragment.SYSTEM_FINDER_FRAGMENT_TAG);
                currentSubtitle = getString(R.string.edsm_credits);
                break;
            }
            case R.id.nav_distance_calculator: {
                switchFragment(DistanceCalculatorFragment.DISTANCE_CALCULATOR_FRAGMENT_TAG);
                currentSubtitle = getString(R.string.eddb_credits);
                break;
            }
            case R.id.nav_commodity_finder: {
                switchFragment(CommodityFinderFragment.COMMODITY_FINDER_FRAGMENT_TAG);
                currentSubtitle = getString(R.string.eddb_eddn_credits);
                break;
            }
            case R.id.nav_ship_finder: {
                switchFragment(ShipFinderFragment.SHIP_FINDER_FRAGMENT_TAG);
                currentSubtitle = getString(R.string.eddb_credits);
                break;
            }
            case R.id.nav_about: {
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return false;
            }
            case R.id.nav_settings: {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return false;
            }
        }

        updateActionBar();
        return true;
    }

    private void switchFragment(String tag) {
        currentFragmentTag = tag;

        // Get previous fragment if exists
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContent,
                    fragment, tag).commit();
            return;
        }

        // Else
        Bundle args = new Bundle();
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
            case SystemFinderFragment.SYSTEM_FINDER_FRAGMENT_TAG:
                ViewUtils.switchFragment(fragmentManager, new SystemFinderFragment(), tag);
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
    }

    private void expandToolbar() {
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        if (appBarLayout != null) {
            appBarLayout.setExpanded(true);
        }
    }

    private void fixToolbarElevation() {
        // Set toolbar elevation to prevent shadow with tabLayout
        if (currentFragmentTag.equals(CommanderFragment.COMMANDER_FRAGMENT)) {
            ViewUtils.setToolbarElevation(appBarLayout, 0);
        } else {
            ViewUtils.setToolbarElevation(appBarLayout, ViewUtils.dpToPx(this, 4));
        }
    }
}