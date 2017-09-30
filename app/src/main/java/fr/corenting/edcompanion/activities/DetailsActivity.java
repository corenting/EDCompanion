package fr.corenting.edcompanion.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter;
import fr.corenting.edcompanion.adapters.GalnetAdapter;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.models.GalnetNews;
import fr.corenting.edcompanion.utils.ThemeUtils;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private CommunityGoal communityGoal;
    private GalnetNews article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set theme first before parent call
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getDarkThemeValue(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ThemeUtils.setToolbarColor(this, toolbar);

        // Get the goal or the article
        communityGoal = getIntent().getExtras().getParcelable("goal");
        if(communityGoal == null) {
            article = getIntent().getExtras().getParcelable("article");
            galnetArticleSetup();
        }
        else
        {
            communityGoalSetup();
        }
    }

    private void communityGoalSetup()
    {
        getSupportActionBar().setTitle(communityGoal.getTitle());

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CommunityGoalsAdapter adapter = new CommunityGoalsAdapter(this, recyclerView, true);
        recyclerView.setAdapter(adapter);
        adapter.addGoal(communityGoal);
    }

    private void galnetArticleSetup()
    {
        getSupportActionBar().setTitle(article.getTitle());

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        GalnetAdapter adapter = new GalnetAdapter(this, recyclerView, true);
        recyclerView.setAdapter(adapter);
        adapter.addNews(article);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
