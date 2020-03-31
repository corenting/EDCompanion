package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.Ship;
import fr.corenting.edcompanion.utils.MathUtils;

public class CommanderFleetAdapter extends androidx.recyclerview.widget.ListAdapter<Ship,
        CommanderFleetAdapter.shipViewHolder> {

    private final NumberFormat numberFormat;
    private Context context;


    public CommanderFleetAdapter(Context ctx) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<Ship>() {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fleet_ship,
                parent, false);
        return new shipViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final shipViewHolder holder, final int position) {
        Ship currentShip = getItem(holder.getAdapterPosition());

        // Name / model ...
        holder.titleTextView.setText(currentShip.getModel());
        if (currentShip.getName() != null && !currentShip.getName().equals(currentShip.getModel())) {
            holder.subtitleTextView.setVisibility(View.VISIBLE);
            holder.subtitleTextView.setText(currentShip.getName());
        } else {
            holder.subtitleTextView.setVisibility(View.GONE);
        }

        // Current ship
        holder.currentShipLabelTextView.setVisibility(currentShip.isCurrentShip() ?
                View.VISIBLE : View.GONE);

        // System and station
        holder.systemTextView.setText(currentShip.getSystemName());
        holder.stationTextView.setText(currentShip.getStationName());

        // Ship value
        String shipValue = numberFormat.format(currentShip.getHullValue() +
                currentShip.getModulesValue());
        holder.shipValueTextView.setText(context.getString(R.string.credits, shipValue));

        // Cargo value
        if (currentShip.getCargoValue() != 0) {
            holder.cargoValueTextView.setVisibility(View.VISIBLE);
            holder.cargoValueLabelTextView.setVisibility(View.VISIBLE);

            String cargoValue = numberFormat.format(currentShip.getCargoValue());
            holder.cargoValueTextView.setText(context.getString(R.string.credits, cargoValue));
        } else {
            holder.cargoValueTextView.setVisibility(View.GONE);
            holder.cargoValueLabelTextView.setVisibility(View.GONE);
        }

        // Ship picture
        Glide.with(holder.shipImageView)
                .load(String.format("https://ed-api.9cw.eu/ships/%s/picture", currentShip.getModel()))
                .error(R.drawable.ship_placeholder)
                .centerCrop()
                .into(holder.shipImageView);
    }

    public static class shipViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.subtitleTextView)
        TextView subtitleTextView;
        @BindView(R.id.currentShipLabelTextView)
        TextView currentShipLabelTextView;
        @BindView(R.id.systemTextView)
        TextView systemTextView;
        @BindView(R.id.stationTextView)
        TextView stationTextView;
        @BindView(R.id.shipValueTextView)
        TextView shipValueTextView;
        @BindView(R.id.cargoValueLabelTextView)
        TextView cargoValueLabelTextView;
        @BindView(R.id.cargoValueTextView)
        TextView cargoValueTextView;
        @BindView(R.id.shipImageView)
        ImageView shipImageView;

        shipViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
