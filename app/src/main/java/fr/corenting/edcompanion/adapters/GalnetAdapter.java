package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.DetailsActivity;
import fr.corenting.edcompanion.models.GalnetNews;
import fr.corenting.edcompanion.utils.DateUtils;

public class GalnetAdapter extends RecyclerView.Adapter<GalnetAdapter.newsViewHolder> {

    private List<GalnetNews> news;
    private Context context;
    private final RecyclerView recyclerView;
    private DateFormat dateFormat;
    private View.OnClickListener onClickListener;
    private boolean isDetailsView;


    public GalnetAdapter(Context ctx, final RecyclerView recyclerView, boolean isDetailsView) {
        this.context = ctx;
        this.recyclerView = recyclerView;
        this.isDetailsView = isDetailsView;
        this.news = new LinkedList<>();
        this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,
                DateUtils.getCurrentLocale(context));

        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GalnetNews article = news.get(recyclerView.getChildAdapterPosition(v));
                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("article", article);
                context.startActivity(i);
            }
        };
    }

    public void addNews(GalnetNews newItem)
    {
        news.add(newItem);
        notifyItemInserted(news.size() - 1);
    }

    public void clearNews()
    {
        news.clear();
        notifyDataSetChanged();
    }

    @Override
    public newsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        v.setOnClickListener(onClickListener);
        return new newsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final newsViewHolder holder, final int position) {
        GalnetNews currentNews = news.get(position);

        // News content
        holder.titleTextView.setText(currentNews.getTitle());
        holder.descriptionTextView.setText(currentNews.getContent());
        if (isDetailsView) {
            holder.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        }

        // Date subtitle
        Date date = new Date((long) currentNews.getDateTimestamp() * 1000);
        holder.subtitleTextView.setText(dateFormat.format(date));

        // Hide the others
        holder.remainingContainer.setVisibility(View.GONE);
        holder.peopleContainer.setVisibility(View.GONE);
        holder.tierContainer.setVisibility(View.GONE);
        holder.locationContainer.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class newsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTextView) TextView titleTextView;
        @BindView(R.id.subtitleTextView) TextView subtitleTextView;
        @BindView(R.id.descriptionTextView) TextView descriptionTextView;
        @BindView(R.id.remainingContainer) RelativeLayout remainingContainer;
        @BindView(R.id.peopleContainer) RelativeLayout peopleContainer;
        @BindView(R.id.tierContainer) RelativeLayout tierContainer;
        @BindView(R.id.locationContainer) RelativeLayout locationContainer;

        newsViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
