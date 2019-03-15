package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.Station;
import fr.corenting.edcompanion.utils.MathUtils;

public class SystemStationsAdapter extends androidx.recyclerview.widget.ListAdapter<Station, SystemStationsAdapter.stationViewHolder> {

    private final NumberFormat numberFormat;
    private final View.OnClickListener onClickListener;
    private Context context;


    public SystemStationsAdapter(Context ctx, final RecyclerView recyclerView) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<Station>() {
            @Override
            public boolean areItemsTheSame(@NonNull Station oldItem,
                                           @NonNull Station newItem) {
                return areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Station oldItem,
                                              @NonNull Station newItem) {
                return oldItem.getName().equals(newItem.getName()) &&
                        oldItem.getSystemName().equals(newItem.getSystemName());
            }
        });

        this.context = ctx;
        this.numberFormat = MathUtils.getNumberFormat(ctx);

        this.onClickListener = v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            if (getItemCount() < position || getItemCount() == 0) {
                return;
            }
            final Station station = getItem(recyclerView.getChildAdapterPosition(v));
            // : TODO : go to station details
            /*Intent i = new Intent(context, DetailsActivity.class);
            i.putExtra("article", article);

            MiscUtils.startIntentWithFadeAnimation(context, i);*/
        };
    }

    @NonNull
    @Override
    public stationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_station,
                parent, false);
        v.setOnClickListener(onClickListener);
        return new stationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final stationViewHolder holder, final int position) {
        Station currentStation = getItem(holder.getAdapterPosition());

        holder.titleTextView.setText(currentStation.getName());
        holder.typeTextView.setText(currentStation.getType());
        holder.starDistanceTextView.setText(context.getString(R.string.distance_ls,
                numberFormat.format(currentStation.getDistanceToStar())));
        holder.landingPadTextView.setText(currentStation.getMaxLandingPad());

        holder.logoImageView.setVisibility(currentStation.isPlanetary() ? View.VISIBLE : View.GONE);
    }

    public static class stationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.logoImageView)
        ImageView logoImageView;
        @BindView(R.id.typeTextView)
        TextView typeTextView;
        @BindView(R.id.starDistanceTextView)
        TextView starDistanceTextView;
        @BindView(R.id.landingPadTextView)
        TextView landingPadTextView;

        stationViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
