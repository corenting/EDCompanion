package fr.corenting.edcompanion.activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        ThemeUtils.setTheme(this);
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Viewpager
        SystemDetailsPagerAdapter pagerAdapter =
                new SystemDetailsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(pagerAdapter);

        // TabLayout
        tabLayout.setupWithViewPager(viewPager);
        if (ThemeUtils.isDarkThemeEnabled(this)) {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.darkPrimary));
        } else {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        tabLayout.setTabTextColors(getResources().getColor(R.color.tabTextSelected),
                getResources().getColor(R.color.tabText));


        getData();
    }

    public void getData() {
        SystemNetwork.getSystemDetails(this, systemName);
        SystemNetwork.getSystemHistory(this, systemName);
        SystemNetwork.getSystemStations(this, systemName);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
