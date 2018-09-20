package fr.corenting.edcompanion.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.SystemDetailsPagerAdapter;
import fr.corenting.edcompanion.network.SystemNetwork;
import fr.corenting.edcompanion.utils.ThemeUtils;

public class SystemDetailsActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;

    private String systemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set theme first before parent call
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getDarkThemeValue(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_with_tabs);
        ButterKnife.bind(this);

        // Get system name from intent
        systemName = "Sol";
        if (getIntent().getExtras() != null) {
            systemName = getIntent().getExtras()
                    .getString(getString(R.string.system), "Sol");
        }

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ThemeUtils.setToolbarColor(this, toolbar);

        // Viewpager
        SystemDetailsPagerAdapter pagerAdapter=
                new SystemDetailsPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(pagerAdapter);

        // TabLayout
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabTextColors(getResources().getColor(R.color.tabTextSelected),
                getResources().getColor(R.color.tabText));



        getData();
    }

    public void getData() {
        SystemNetwork.getSystemDetails(this, systemName);
        SystemNetwork.getSystemHistory(this, systemName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
