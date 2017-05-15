package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.GalnetNews;

public class GalnetAdapter extends RecyclerView.Adapter<GalnetAdapter.newsViewHolder> {

    private List<GalnetNews> news;
    private Context context;

    public GalnetAdapter(final Context context, final RecyclerView recyclerView) {
        this.context = context;
        this.news = new LinkedList<>();
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
        return new newsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final newsViewHolder holder, final int position) {
        GalnetNews currentNews = news.get(position);
        String dateString = new Date(currentNews.getDate()).toString();

        // News content
        holder.titleTextView.setText(currentNews.getTitle());
        holder.descriptionTextView.setText(currentNews.getContent());
        holder.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        holder.subtitleTextView.setText(dateString);

        // Hide the others
        holder.remainingContainer.setVisibility(View.GONE);
        holder.peopleContainer.setVisibility(View.GONE);
        holder.tierContainer.setVisibility(View.GONE);
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

        newsViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
