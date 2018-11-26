package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.Station;
import fr.corenting.edcompanion.utils.MathUtils;

public class SystemStationsAdapter extends ListAdapter<SystemStationsAdapter.stationViewHolder,
        Station> {

    private final NumberFormat numberFormat;
    private final View.OnClickListener onClickListener;
    private Context context;


    public SystemStationsAdapter(Context ctx, final RecyclerView recyclerView) {
        this.context = ctx;
        this.dataSet = new ArrayList<>();
        this.numberFormat = MathUtils.getNumberFormat(ctx);

        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);
                if (dataSet.size() < position || dataSet.size() == 0) {
                    return;
                }
                final Station station = dataSet.get(recyclerView.getChildAdapterPosition(v));
                // : TODO : go to station details
                /*Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("article", article);

                MiscUtils.startIntentWithFadeAnimation(context, i);*/
            }
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
        Station currentStation = dataSet.get(holder.getAdapterPosition());

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
