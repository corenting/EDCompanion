package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.databinding.ListItemFleetShipBinding;
import fr.corenting.edcompanion.models.Ship;
import fr.corenting.edcompanion.utils.MathUtils;

public class CommanderFleetAdapter extends ListAdapter<Ship,
        CommanderFleetAdapter.shipViewHolder> {

    private final NumberFormat numberFormat;
    private final Context context;


    public CommanderFleetAdapter(Context ctx) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Ship oldItem,
                                           @NonNull Ship newItem) {
                return areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Ship oldItem,
                                              @NonNull Ship newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });

        this.context = ctx;
        this.numberFormat = MathUtils.getNumberFormat(ctx);
    }

    @NonNull
    @Override
    public shipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemFleetShipBinding itemBinding = ListItemFleetShipBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new shipViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final shipViewHolder holder, final int position) {
        Ship currentShip = getItem(holder.getAdapterPosition());

        // Name / model ...
        holder.viewBinding.titleTextView.setText(currentShip.getModel());
        if (currentShip.getName() != null && !currentShip.getName().equals(currentShip.getModel())) {
            holder.viewBinding.subtitleTextView.setVisibility(View.VISIBLE);
            holder.viewBinding.subtitleTextView.setText(currentShip.getName());
        } else {
            holder.viewBinding.subtitleTextView.setVisibility(View.GONE);
        }

        // Current ship
        holder.viewBinding.currentShipLabelTextView.setVisibility(currentShip.isCurrentShip() ?
                View.VISIBLE : View.GONE);

        // System and station
        holder.viewBinding.systemTextView.setText(currentShip.getSystemName());
        holder.viewBinding.stationTextView.setText(currentShip.getStationName());

        // Ship value
        String shipValue = numberFormat.format(currentShip.getHullValue() +
                currentShip.getModulesValue());
        holder.viewBinding.shipValueTextView.setText(context.getString(R.string.credits, shipValue));

        // Cargo value
        if (currentShip.getCargoValue() != 0) {
            holder.viewBinding.cargoValueTextView.setVisibility(View.VISIBLE);
            holder.viewBinding.cargoValueLabelTextView.setVisibility(View.VISIBLE);

            String cargoValue = numberFormat.format(currentShip.getCargoValue());
            holder.viewBinding.cargoValueTextView.setText(context.getString(R.string.credits, cargoValue));
        } else {
            holder.viewBinding.cargoValueTextView.setVisibility(View.GONE);
            holder.viewBinding.cargoValueLabelTextView.setVisibility(View.GONE);
        }

        // Ship picture
        String shipUrl = String.format("https://ed.9cw.eu/ships/%s/picture", currentShip.getInternalModel());
        Glide.with(holder.viewBinding.shipImageView)
                .load(shipUrl)
                .error(R.drawable.ship_placeholder)
                .centerCrop()
                .into(holder.viewBinding.shipImageView);
    }

    public static class shipViewHolder extends RecyclerView.ViewHolder {
        private final ListItemFleetShipBinding viewBinding;

        shipViewHolder(final ListItemFleetShipBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
