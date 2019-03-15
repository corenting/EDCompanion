package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.DetailsActivity;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.utils.MiscUtils;
import fr.corenting.edcompanion.views.TableView;

public class CommunityGoalsAdapter extends androidx.recyclerview.widget.ListAdapter<CommunityGoal,
        CommunityGoalsAdapter.goalsViewHolder> {

    private View.OnClickListener onClickListener;

    private Context context;
    private boolean isDetailsView;

    public CommunityGoalsAdapter(final Context context, final RecyclerView recyclerView, boolean isDetailsView) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<CommunityGoal>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommunityGoal oldItem,
                                           @NonNull CommunityGoal newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommunityGoal oldItem,
                                              @NonNull CommunityGoal newItem) {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                        oldItem.getDescription().equals(newItem.getDescription()) &&
                        (oldItem.getDistanceToPlayer() != null || newItem.getDistanceToPlayer() == null);
            }
        });

        this.context = context;
        this.isDetailsView = isDetailsView;

        onClickListener = v -> switchToDetailsView(recyclerView.getChildAdapterPosition(v));
    }

    @NonNull
    @Override
    public goalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_community_goal,
                parent, false);
        if (!isDetailsView) {
            v.setOnClickListener(onClickListener);
        }
        return new goalsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final goalsViewHolder holder, final int position) {
        final CommunityGoal currentGoal = getItem(holder.getAdapterPosition());

        // Content
        holder.titleTextView.setText(currentGoal.getTitle());
        holder.updateTextView.setText(currentGoal.getRefreshDateString(context));
        holder.peopleTextView.setText(context.getString(R.string.cg_contributors,
                String.valueOf(currentGoal.getContributors())));
        holder.remainingTextView.setText(context.getString(R.string.cg_end_date,
                currentGoal.getEndDate(context)));
        holder.tierTextView.setText(context.getString(R.string.cg_progress,
                currentGoal.getTierString()));

        // System
        if (currentGoal.getDistanceToPlayer() != null) {
            holder.locationTextView.setText(context.getString(R.string.cg_system_with_player_distance,
                    currentGoal.getSystem(), currentGoal.getDistanceToPlayer()));
        } else {
            holder.locationTextView.setText(context.getString(R.string.cg_system_distance,
                    currentGoal.getSystem()));
        }
        holder.locationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MiscUtils.startIntentToSystemDetails(context, currentGoal.getSystem());
            }
        });

        // Objective
        String objective = context.getString(R.string.no_objective_yet);
        if (currentGoal.getObjective().length() != 0) {
            objective = currentGoal.getObjective();
        }
        holder.objectiveTextView.setText(objective);

        // Description
        if (isDetailsView) {
            holder.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        }
        String description = context.getString(R.string.no_description_yet);
        if (currentGoal.getDescription().length() != 0) {
            description = currentGoal.getDescription();
        }
        holder.descriptionTextView.setText(description);

        // Rewards table
        if (currentGoal.getRewards().size() != 0) {
            setRewards(holder, currentGoal);
        } else {
            holder.rewardsTableView.setVisibility(View.GONE);
        }

        // Set click listeners
        setClickListeners(holder.getAdapterPosition(), holder.peopleTextView, R.string.hint_participants);
        setClickListeners(holder.getAdapterPosition(), holder.remainingTextView, R.string.hint_end_date);
        setClickListeners(holder.getAdapterPosition(), holder.tierTextView, R.string.hint_tiers);
        setClickListeners(holder.getAdapterPosition(), holder.locationTextView, R.string.hint_system);
        setClickListeners(holder.getAdapterPosition(), holder.descriptionTextView,
                R.string.community_goal_description);
        setClickListeners(holder.getAdapterPosition(), holder.objectiveTextView, R.string.objective);
    }

    private void setRewards(goalsViewHolder holder, CommunityGoal goal) {
        // Set headers
        holder.rewardsTableView.setHeaders(context.getString(R.string.tier),
                context.getString(R.string.contributions),
                context.getString(R.string.reward));

        // Set content
        holder.rewardsTableView.setContent(goal.getRewards());
    }

    private void setClickListeners(final int position, final TextView textView,
                                   final int labelResId) {
        // Set long click listener for copying informations to clipboard
        textView.setOnLongClickListener(null);
        textView.setOnLongClickListener(view -> {
            MiscUtils.putTextInClipboard(context, context.getString(labelResId),
                    textView.getText().toString(), true);
            return true;
        });

        // Setup a regular click listener for details view except for location text view
        if (!isDetailsView && textView.getId() != R.id.locationTextView) {
            textView.setOnClickListener(null);
            textView.setOnClickListener(v -> switchToDetailsView(position));
        }
    }

    private void switchToDetailsView(int position) {
        if (getItemCount() < position || getItemCount() == 0) {
            return;
        }
        final CommunityGoal goal = getItem(position);
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra("goal", goal);

        MiscUtils.startIntentWithFadeAnimation(context, i);
    }

    public static class goalsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.objectiveTextView)
        TextView objectiveTextView;
        @BindView(R.id.updateTextView)
        TextView updateTextView;
        @BindView(R.id.descriptionTextView)
        TextView descriptionTextView;
        @BindView(R.id.remainingTextView)
        TextView remainingTextView;
        @BindView(R.id.tierTextView)
        TextView tierTextView;
        @BindView(R.id.peopleTextView)
        TextView peopleTextView;
        @BindView(R.id.locationTextView)
        TextView locationTextView;
        @BindView(R.id.rewardsTableView)
        TableView rewardsTableView;


        goalsViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            objectiveTextView.setVisibility(View.VISIBLE);
        }
    }
}
