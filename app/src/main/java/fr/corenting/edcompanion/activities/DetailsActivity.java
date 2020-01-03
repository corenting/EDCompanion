package fr.corenting.edcompanion.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter;
import fr.corenting.edcompanion.adapters.NewsAdapter;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.models.NewsArticle;
import fr.corenting.edcompanion.utils.ThemeUtils;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Common recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Get the goal or the article
        if (getIntent().getExtras() != null) {
            CommunityGoal communityGoal = getIntent().getExtras().getParcelable("goal");
            NewsArticle article = getIntent().getExtras().getParcelable("article");
            if (communityGoal == null && article != null) {
                galnetArticleSetup(article);
            } else if (communityGoal != null) {
                communityGoalSetup(communityGoal);
            }
        }

        recyclerView.smoothScrollToPosition(-10); // because recycler view may not start on top
    }

    private void communityGoalSetup(CommunityGoal communityGoal) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(communityGoal.getTitle());
        }

        // Adapter setup
        CommunityGoalsAdapter adapter = new CommunityGoalsAdapter(this, recyclerView, true);
        recyclerView.setAdapter(adapter);
        adapter.submitList(Collections.singletonList(communityGoal));
    }

    private void galnetArticleSetup(NewsArticle article) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(article.getTitle());
        }

        // Adapter view setup
        NewsAdapter adapter = new NewsAdapter(this, recyclerView, true);
        recyclerView.setAdapter(adapter);
        adapter.submitList(Collections.singletonList(article));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
