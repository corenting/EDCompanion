package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.DetailsActivity;
import fr.corenting.edcompanion.models.GalnetArticle;
import fr.corenting.edcompanion.utils.DateUtils;
import fr.corenting.edcompanion.utils.MiscUtils;

public class GalnetAdapter extends androidx.recyclerview.widget.ListAdapter<GalnetArticle,
        GalnetAdapter.newsViewHolder> {

    private Context context;
    private DateFormat dateFormat;
    private View.OnClickListener onClickListener;
    private boolean isDetailsView;


    public GalnetAdapter(Context ctx, final RecyclerView recyclerView, boolean isDetailsView) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<GalnetArticle>() {
            @Override
            public boolean areItemsTheSame(@NonNull GalnetArticle oldItem,
                                           @NonNull GalnetArticle newItem) {
                return areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull GalnetArticle oldItem,
                                              @NonNull GalnetArticle newItem) {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                        oldItem.getDateTimestamp() == newItem.getDateTimestamp();
            }
        });

        this.context = ctx;
        this.isDetailsView = isDetailsView;
        this.dateFormat = DateFormat.getDateInstance(DateFormat.LONG,
                DateUtils.getCurrentLocale(context));

        this.onClickListener = v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            if (getItemCount() < position || getItemCount() == 0) {
                return;
            }
            final GalnetArticle article = getItem(recyclerView.getChildAdapterPosition(v));
            Intent i = new Intent(context, DetailsActivity.class);
            i.putExtra("article", article);

            MiscUtils.startIntentWithFadeAnimation(context, i);
        };
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_galnet,
                parent, false);
        if (!isDetailsView) {
            v.setOnClickListener(onClickListener);
        }
        return new newsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final newsViewHolder holder, final int position) {
        GalnetArticle currentNews = getItem(holder.getAdapterPosition());

        // News content
        holder.titleTextView.setText(currentNews.getTitle());
        holder.descriptionTextView.setText(currentNews.getContent());
        if (isDetailsView) {
            holder.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        }

        // Date subtitle
        Date date = new Date(currentNews.getDateTimestamp() * 1000);
        holder.dateTextView.setText(dateFormat.format(date));
    }

    public static class newsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.dateTextView)
        TextView dateTextView;
        @BindView(R.id.descriptionTextView)
        TextView descriptionTextView;

        newsViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
