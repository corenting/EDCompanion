package fr.corenting.edcompanion.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommunityGoal;

public class CommunityGoalDetailsActivity extends AppCompatActivity {

    @BindView(R.id.titleTextView) TextView titleTextView;
    @BindView(R.id.subtitleTextView) TextView subtitleTextView;
    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.remainingTextView) TextView remainingTextView;
    @BindView(R.id.tierTextView) TextView tierTextView;
    @BindView(R.id.peopleTextView) TextView peopleTextView;

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

        // Set the views
        getSupportActionBar().setTitle(communityGoal.getTitle());
        titleTextView.setText(communityGoal.getTitle());
        descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        descriptionTextView.setText(communityGoal.getDescription());
        peopleTextView.setText(String.valueOf(communityGoal.getContributors()));
        subtitleTextView.setText(communityGoal.getRefreshDateString(this));
        remainingTextView.setText(communityGoal.getRemainingString());
        tierTextView.setText(communityGoal.getTierString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
