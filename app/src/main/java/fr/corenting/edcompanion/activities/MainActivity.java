package fr.corenting.edcompanion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

        // Setup navigation view and fake click the first item
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set initial fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction().replace(R.id.fragmentContent, new CommunityGoalsFragment())
                .commit();

        // Select the first item in menu as the fragment was loaded
        navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());
        setTitle(getString(R.string.community_goals));
        getSupportActionBar().setSubtitle(R.string.data_credits);

        // Update the server status
        updateServerStatus();

        // Set listener on server status text to refresh it
        TextView drawerSubtitleTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
        drawerSubtitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateServerStatus();
            }
        });
    }

    private void updateServerStatus() {
        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
        textView.setText(getString(R.string.updating_server_status));
        ServerStatusNetwork.getStatus(this);
    }

    @Subscribe
    public void onServerStatusEvent(String status) {
        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerSubtitleTextView);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Let's update the server status too
        updateServerStatus();


        if (id == R.id.nav_cg) {
            fragmentManager
                    .beginTransaction().replace(R.id.fragmentContent, new CommunityGoalsFragment())
                    .commit();
            setTitle(getString(R.string.community_goals));
            getSupportActionBar().setSubtitle(R.string.data_credits);
        } else if (id == R.id.nav_cmdr) {
            fragmentManager
                    .beginTransaction().replace(R.id.fragmentContent, new StatusFragment())
                    .commit();

            String commanderName = SettingsUtils.getCommanderName(this);
            setTitle(commanderName.equals("") ?  getString(R.string.status) : commanderName);
            getSupportActionBar().setSubtitle("");
        } else if (id == R.id.nav_galnet_news) {
            // Create the fragment with the arguments
            GalnetFragment fragment = new GalnetFragment();
            Bundle args = new Bundle();
            args.putBoolean("reportsMode", false);
            fragment.setArguments(args);
            // Change fragment
            fragmentManager
                    .beginTransaction().replace(R.id.fragmentContent, fragment)
                    .commit();
            setTitle(getString(R.string.galnet));
            getSupportActionBar().setSubtitle("");
        } else if (id == R.id.nav_galnet_reports) {
            // Create the fragment with the arguments
            GalnetFragment fragment = new GalnetFragment();
            Bundle args = new Bundle();
            args.putBoolean("reportsMode", true);
            fragment.setArguments(args);
            // Change fragment
            fragmentManager
                    .beginTransaction().replace(R.id.fragmentContent, fragment)
                    .commit();
            setTitle(getString(R.string.galnet_reports));
            getSupportActionBar().setSubtitle("");
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
