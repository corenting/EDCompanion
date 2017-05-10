package fr.corenting.edcompanion.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.activities.CommunityGoalDetailsActivity;
import fr.corenting.edcompanion.fragments.CommunityGoalsFragment;
import fr.corenting.edcompanion.models.CommunityGoal;

import fr.corenting.edcompanion.R;

public class CommunityGoalsAdapter extends RecyclerView.Adapter<CommunityGoalsAdapter.goalsViewHolder> {

    private List<CommunityGoal> goals;
    private View.OnClickListener onClickListener;
    private CommunityGoalsFragment parent;
    private PrettyTime prettyTime;

    public CommunityGoalsAdapter(final CommunityGoalsFragment parent) {
        this.parent = parent;
        this.goals = new LinkedList<>();
        prettyTime = new PrettyTime(Locale.US);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CommunityGoal goal = goals.get(parent.recyclerView.getChildAdapterPosition(v));
                Intent i = new Intent(parent.getContext(), CommunityGoalDetailsActivity.class);
                i.putExtra("goal", goal);
                parent.startActivity(i);
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
        parent.endLoading(0);
        goals.clear();
        notifyDataSetChanged();
    }

    @Override
    public goalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_list_item, parent, false);
        v.setOnClickListener(onClickListener);
        return new goalsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final goalsViewHolder holder, final int position) {
        CommunityGoal currentGoal = goals.get(position);

        holder.titleTextView.setText(currentGoal.getTitle());
        holder.descriptionTextView.setText(currentGoal.getDescription());
        holder.peopleTextView.setText(String.valueOf(currentGoal.getContributors()));

        // Last update
        if (currentGoal.getRefreshDate().equals("finished")) {
            holder.subtitleTextView.setText(parent.getString(R.string.last_update, currentGoal.getRefreshDate()));
        }
        else {
            try {
                DateFormat sourceFormat = new SimpleDateFormat("dd MMM yyyy, hh:mma", Locale.US);
                Date date = sourceFormat.parse(currentGoal.getRefreshDate());
                holder.subtitleTextView.setText(parent.getString(R.string.last_update, prettyTime.format(date)));
            }
            catch (Exception e)
            {
                holder.subtitleTextView.setText(parent.getString(R.string.last_update, parent.getString(R.string.unknown)));
            }
        }


        // Remaining
        String remainingText = currentGoal.isOngoing() ? currentGoal.getEndDate() : "Finished";
        holder.remainingTextView.setText(remainingText);

        // Tier
        String tierText = new StringBuilder()
                .append(currentGoal.getCurrentTier())
                .append(" / ")
                .append(currentGoal.getTotalTier()).toString();
        holder.tierTextView.setText(tierText);
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

        goalsViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
