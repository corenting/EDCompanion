package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.activities.CommunityGoalDetailsActivity;
import fr.corenting.edcompanion.models.CommunityGoal;

import fr.corenting.edcompanion.R;

public class CommunityGoalsAdapter extends RecyclerView.Adapter<CommunityGoalsAdapter.goalsViewHolder> {

    private List<CommunityGoal> goals;
    private View.OnClickListener onClickListener;
    private Context context;
    private boolean isDetailsView;

    public CommunityGoalsAdapter(final Context context, final RecyclerView recyclerView, boolean isDetailsView) {
        this.context = context;
        this.isDetailsView = isDetailsView;
        this.goals = new LinkedList<>();

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CommunityGoal goal = goals.get(recyclerView.getChildAdapterPosition(v));
                Intent i = new Intent(context, CommunityGoalDetailsActivity.class);
                i.putExtra("goal", goal);
                context.startActivity(i);
            }
        };
    }

    public void addGoal(CommunityGoal goal)
    {
        goals.add(goal);
        notifyItemInserted(goals.size() - 1);
    }

    public void clearGoals()
    {
        goals.clear();
        notifyDataSetChanged();
    }

    @Override
    public goalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        v.setOnClickListener(onClickListener);
        return new goalsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final goalsViewHolder holder, final int position) {
        CommunityGoal currentGoal = goals.get(position);

        holder.titleTextView.setText(currentGoal.getTitle());
        holder.descriptionTextView.setText(currentGoal.getDescription());
        if (isDetailsView) {
            holder.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        }
        holder.peopleTextView.setText(String.valueOf(currentGoal.getContributors()));
        holder.subtitleTextView.setText(currentGoal.getRefreshDateString(context));
        holder.remainingTextView.setText(currentGoal.getRemainingString());
        holder.tierTextView.setText(currentGoal.getTierString());
        holder.locationTextView.setText(currentGoal.getSystem());
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class goalsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView) TextView titleTextView;
        @BindView(R.id.subtitleTextView) TextView subtitleTextView;
        @BindView(R.id.descriptionTextView) TextView descriptionTextView;
        @BindView(R.id.remainingTextView) TextView remainingTextView;
        @BindView(R.id.tierTextView) TextView tierTextView;
        @BindView(R.id.peopleTextView) TextView peopleTextView;
        @BindView(R.id.locationTextView) TextView locationTextView;

        goalsViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
