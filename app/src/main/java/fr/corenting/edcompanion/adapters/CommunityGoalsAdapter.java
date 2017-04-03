package fr.corenting.edcompanion.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.fragments.CommunityGoalsFragment;
import fr.corenting.edcompanion.models.CommunityGoal;

import fr.corenting.edcompanion.R;

public class CommunityGoalsAdapter extends RecyclerView.Adapter<CommunityGoalsAdapter.goalsViewHolder> {

    public List<CommunityGoal> goals;
    private View.OnClickListener onClickListener;
    private CommunityGoalsFragment parent;

    public CommunityGoalsAdapter(final CommunityGoalsFragment parent) {
        this.parent = parent;
        this.goals = new LinkedList<>();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                final CustomTypes.Comic comic = comics.get(parent.recyclerView.getChildAdapterPosition(v));
                Intent i = new Intent(parent, ComicInfoActivity.class);
                i.putExtra("comic", comic);
                parent.startActivityForResult(i, 1);*/
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
