package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.databinding.ListItemLoadoutBinding;
import fr.corenting.edcompanion.models.CommanderLoadout;
import fr.corenting.edcompanion.models.CommanderLoadoutWeapon;

public class CommanderLoadoutsAdapter extends ListAdapter<CommanderLoadout,
        CommanderLoadoutsAdapter.loadoutViewHolder> {

    private final Context context;


    public CommanderLoadoutsAdapter(Context ctx) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommanderLoadout oldItem,
                                           @NonNull CommanderLoadout newItem) {
                return areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommanderLoadout oldItem,
                                              @NonNull CommanderLoadout newItem) {
                return oldItem.getLoadoutId() == newItem.getLoadoutId();
            }
        });

        this.context = ctx;
    }

    @NonNull
    @Override
    public loadoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemLoadoutBinding itemBinding = ListItemLoadoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new loadoutViewHolder(itemBinding);
    }

    private void setLoadoutWeaponDisplay(CommanderLoadoutWeapon weapon, TextView labelTextView, TextView textView) {
        if (weapon != null) {
            labelTextView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);

            if (weapon.getMagazineName() != null) {
                textView.setText(context.getString(
                        R.string.weapon_display,
                        weapon.getName(),
                        weapon.getMagazineName()
                ));
            }
            else {
                textView.setText(context.getString(
                        R.string.weapon_display_without_magazine,
                        weapon.getName()
                ));
            }

        } else {
            labelTextView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final loadoutViewHolder holder, final int position) {
        CommanderLoadout currentLoadout = getItem(holder.getAdapterPosition());

        // Name
        if (currentLoadout.getLoadoutName() != null) {
            holder.viewBinding.currentLoadoutLayout.loadoutTextView.setText(currentLoadout.getLoadoutName());
        } else {
            holder.viewBinding.currentLoadoutLayout.loadoutTextView.setText(context.getString(R.string.loadout_with_number, currentLoadout.getLoadoutId()));
        }

        holder.viewBinding.currentLoadoutLayout.suitTextView.setText(currentLoadout.getSuitName());

        // Weapons
        setLoadoutWeaponDisplay(
                currentLoadout.getFirstPrimaryWeapon(),
                holder.viewBinding.currentLoadoutLayout.firstPrimaryWeaponLabelTextView,
                holder.viewBinding.currentLoadoutLayout.firstPrimaryWeaponTextView
        );
        setLoadoutWeaponDisplay(
                currentLoadout.getSecondPrimaryWeapon(),
                holder.viewBinding.currentLoadoutLayout.secondaryPrimaryWeaponLabelTextView,
                holder.viewBinding.currentLoadoutLayout.secondaryPrimaryWeaponTextView
        );
        setLoadoutWeaponDisplay(
                currentLoadout.getSecondaryWeapon(),
                holder.viewBinding.currentLoadoutLayout.secondaryWeaponLabelTextView,
                holder.viewBinding.currentLoadoutLayout.secondarWeaponTextView
        );
    }

    public static class loadoutViewHolder extends RecyclerView.ViewHolder {
        private final ListItemLoadoutBinding viewBinding;

        loadoutViewHolder(final ListItemLoadoutBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
