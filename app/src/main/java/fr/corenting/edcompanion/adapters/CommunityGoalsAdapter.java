package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.DetailsActivity;
import fr.corenting.edcompanion.databinding.ListItemCommunityGoalBinding;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.utils.MiscUtils;

public class CommunityGoalsAdapter extends ListAdapter<CommunityGoal,
        CommunityGoalsAdapter.goalsViewHolder> {

    private final View.OnClickListener onClickListener;

    private final Context context;
    private final boolean isDetailsView;

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
        ListItemCommunityGoalBinding itemBinding = ListItemCommunityGoalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        if (!isDetailsView) {
            itemBinding.getRoot().setOnClickListener(onClickListener);
        }
        return new goalsViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final goalsViewHolder holder, final int position) {
        final CommunityGoal currentGoal = getItem(holder.getAdapterPosition());

        // Content
        holder.viewBinding.titleTextView.setText(currentGoal.getTitle());
        holder.viewBinding.updateTextView.setText(currentGoal.getRefreshDateString(context));
        holder.viewBinding.peopleTextView.setText(context.getString(R.string.cg_contributors,
                String.valueOf(currentGoal.getContributors())));
        holder.viewBinding.remainingTextView.setText(context.getString(R.string.cg_end_date,
                currentGoal.getEndDate(context)));
        holder.viewBinding.tierTextView.setText(context.getString(R.string.cg_progress,
                currentGoal.getTierString()));

        // System
        if (currentGoal.getDistanceToPlayer() != null) {
            holder.viewBinding.locationTextView.setText(context.getString(R.string.cg_system_with_player_distance,
                    currentGoal.getSystem(), currentGoal.getDistanceToPlayer()));
        } else {
            holder.viewBinding.locationTextView.setText(context.getString(R.string.cg_system_distance,
                    currentGoal.getSystem()));
        }
        holder.viewBinding.locationTextView.setOnClickListener(v -> MiscUtils.startIntentToSystemDetails(context, currentGoal.getSystem()));

        // Objective
        String objective = context.getString(R.string.no_objective_yet);
        if (currentGoal.getObjective().length() != 0) {
            objective = currentGoal.getObjective();
        }
        holder.viewBinding.objectiveTextView.setText(objective);

        // Description
        if (isDetailsView) {
            holder.viewBinding.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        }
        String description = context.getString(R.string.no_description_yet);
        if (currentGoal.getDescription().length() != 0) {
            description = currentGoal.getDescription();
        }
        holder.viewBinding.descriptionTextView.setText(description);

        // Set click listeners
        setClickListeners(holder.getAdapterPosition(), holder.viewBinding.peopleTextView, R.string.hint_participants);
        setClickListeners(holder.getAdapterPosition(), holder.viewBinding.remainingTextView, R.string.hint_end_date);
        setClickListeners(holder.getAdapterPosition(), holder.viewBinding.tierTextView, R.string.hint_tiers);
        setClickListeners(holder.getAdapterPosition(), holder.viewBinding.locationTextView, R.string.hint_system);
        setClickListeners(holder.getAdapterPosition(), holder.viewBinding.descriptionTextView,
                R.string.community_goal_description);
        setClickListeners(holder.getAdapterPosition(), holder.viewBinding.objectiveTextView, R.string.objective);
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
        private final ListItemCommunityGoalBinding viewBinding;

        goalsViewHolder(final ListItemCommunityGoalBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            viewBinding.objectiveTextView.setVisibility(View.VISIBLE);
        }
    }
}
