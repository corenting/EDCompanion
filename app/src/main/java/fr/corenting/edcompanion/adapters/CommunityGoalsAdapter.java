package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.DetailsActivity;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.utils.MiscUtils;

public class CommunityGoalsAdapter extends RecyclerView.Adapter<CommunityGoalsAdapter.goalsViewHolder> {

    private List<CommunityGoal> goals;
    private View.OnClickListener onClickListener;

    private Context context;
    private boolean isDetailsView;

    public CommunityGoalsAdapter(final Context context, final RecyclerView recyclerView, final List<CommunityGoal> goals, boolean isDetailsView) {
        this.context = context;
        this.isDetailsView = isDetailsView;
        this.goals = goals;

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToDetailsView(recyclerView.getChildAdapterPosition(v));
            }
        };
    }

    @Override
    public goalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        if (!isDetailsView) {
            v.setOnClickListener(onClickListener);
        }
        return new goalsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final goalsViewHolder holder, final int position) {
        CommunityGoal currentGoal = goals.get(position);

        holder.titleTextView.setText(currentGoal.getTitle());
        holder.objectiveTextView.setText(currentGoal.getObjective());
        holder.subtitleTextView.setText(currentGoal.getRefreshDateString(context));

        holder.descriptionTextView.setText(currentGoal.getDescription());
        if (isDetailsView) {
            holder.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        }
        holder.peopleTextView.setText(String.valueOf(currentGoal.getContributors()));
        holder.remainingTextView.setText(currentGoal.getEndDate(context));
        holder.tierTextView.setText(currentGoal.getTierString());
        holder.locationTextView.setText(currentGoal.getSystem());

        // Set click listeners
        setClickListeners(position, holder.peopleTextView, R.string.hint_participants);
        setClickListeners(position, holder.remainingTextView, R.string.hint_end_date);
        setClickListeners(position, holder.tierTextView, R.string.hint_tiers);
        setClickListeners(position, holder.locationTextView, R.string.hint_system);
        setClickListeners(position, holder.descriptionTextView, R.string.community_goal_description);
        setClickListeners(position, holder.objectiveTextView, R.string.objective);
    }

    private void setClickListeners(final int position, final TextView textView,
                                   final int labelResId) {
        // Set long click listener for copying informations to clipboard
        textView.setOnLongClickListener(null);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MiscUtils.putTextInClipboardWithNotification(context,
                        context.getString(labelResId),
                        textView.getText().toString());
                return true;
            }
        });

        // Setup a regular click listener for details view
        textView.setOnClickListener(null);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToDetailsView(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    private void switchToDetailsView(int position) {
        final CommunityGoal goal = goals.get(position);
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra("goal", goal);
        context.startActivity(i);
    }

    public static class goalsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView) TextView titleTextView;
        @BindView(R.id.objectiveTextView) TextView objectiveTextView;
        @BindView(R.id.subtitleTextView) TextView subtitleTextView;
        @BindView(R.id.descriptionTextView) TextView descriptionTextView;
        @BindView(R.id.remainingTextView) TextView remainingTextView;
        @BindView(R.id.tierTextView) TextView tierTextView;
        @BindView(R.id.peopleTextView) TextView peopleTextView;
        @BindView(R.id.locationTextView) TextView locationTextView;

        goalsViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            objectiveTextView.setVisibility(View.VISIBLE);
        }
    }
}
