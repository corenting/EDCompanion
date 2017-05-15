package fr.corenting.edcompanion.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.CommunityGoalsAdapter;
import fr.corenting.edcompanion.models.CommunityGoal;

public class CommunityGoalDetailsActivity extends AppCompatActivity {

    @BindView(R.id.goalsRecyclerView) RecyclerView goalsRecyclerView;
    private CommunityGoal communityGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_goal_details);
        ButterKnife.bind(this);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the goal
        communityGoal = getIntent().getExtras().getParcelable("goal");
        getSupportActionBar().setTitle(communityGoal.getTitle());

        // Recycler view setup
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        goalsRecyclerView.setLayoutManager(linearLayoutManager);
        CommunityGoalsAdapter adapter = new CommunityGoalsAdapter(this, goalsRecyclerView, true);
        goalsRecyclerView.setAdapter(adapter);
        adapter.addGoal(communityGoal);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
