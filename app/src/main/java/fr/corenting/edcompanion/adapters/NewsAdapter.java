package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.util.Date;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.DetailsActivity;
import fr.corenting.edcompanion.databinding.ListItemGalnetBinding;
import fr.corenting.edcompanion.models.NewsArticle;
import fr.corenting.edcompanion.utils.DateUtils;
import fr.corenting.edcompanion.utils.MiscUtils;

public class NewsAdapter extends androidx.recyclerview.widget.ListAdapter<NewsArticle,
        NewsAdapter.newsViewHolder> {

    private final Context context;
    private final DateFormat dateFormat;
    private final View.OnClickListener onClickListener;
    private final boolean isDetailsView;


    public NewsAdapter(Context ctx, final RecyclerView recyclerView, boolean isDetailsView) {
        // Parent class setup
        super(new DiffUtil.ItemCallback<NewsArticle>() {
            @Override
            public boolean areItemsTheSame(@NonNull NewsArticle oldItem,
                                           @NonNull NewsArticle newItem) {
                return areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull NewsArticle oldItem,
                                              @NonNull NewsArticle newItem) {
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
            final NewsArticle article = getItem(recyclerView.getChildAdapterPosition(v));
            Intent i = new Intent(context, DetailsActivity.class);
            i.putExtra("article", article);

            MiscUtils.startIntentWithFadeAnimation(context, i);
        };
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemGalnetBinding itemBinding = ListItemGalnetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        if (!isDetailsView) {
            itemBinding.getRoot().setOnClickListener(onClickListener);
        }
        return new newsViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final newsViewHolder holder, final int position) {
        NewsArticle currentArticle = getItem(holder.getAdapterPosition());

        // News content
        holder.viewBinding.titleTextView.setText(currentArticle.getTitle());
        MiscUtils.setTextFromHtml(holder.viewBinding.descriptionTextView, currentArticle.getContent());
        if (isDetailsView) {
            holder.viewBinding.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
        }

        // Date subtitle
        Date date = new Date(currentArticle.getDateTimestamp() * 1000);
        holder.viewBinding.dateTextView.setText(dateFormat.format(date));

        // Ship picture
        Glide.with(holder.viewBinding.galnetImageView)
                .load(currentArticle.getPicture())
                .error(R.drawable.galnet_placeholder)
                .centerCrop()
                .into(holder.viewBinding.galnetImageView);
    }

    public static class newsViewHolder extends RecyclerView.ViewHolder {
        private final ListItemGalnetBinding viewBinding;

        newsViewHolder(final ListItemGalnetBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
