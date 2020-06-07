package fr.corenting.edcompanion.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.animation.ProgressBarAnimation;
import fr.corenting.edcompanion.models.events.Ranks;

public class RankUtils {

    public static void setTempContent(Context ctx, View rootView, String name) {
        TextView titleView = rootView.findViewById(R.id.titleTextView);
        TextView progressView = rootView.findViewById(R.id.progressTextView);
        ProgressBar progressBar = rootView.findViewById(R.id.progressBar);

        titleView.setText(String.format("%s : %s", name, ctx.getString(R.string.unknown)));
        progressView.setText(ctx.getResources().getString(R.string.rank_progress, 0));
        progressBar.setProgress(0);
    }

    public static void setContent(Context ctx, View rootView, int logoId, Ranks.Rank rank, String description) {
        ImageView logoView = rootView.findViewById(R.id.itemImageView);
        TextView titleView = rootView.findViewById(R.id.titleTextView);
        TextView progressView = rootView.findViewById(R.id.progressTextView);
        ProgressBar progressBar = rootView.findViewById(R.id.progressBar);

        logoView.setImageResource(logoId);
        titleView.setText(String.format("%s : %s", description, rank.getName()));

        // If rank progress is -1 it means it's not supported by the network used so hide it
        if (rank.getProgress() == -1) {
            progressView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            progressView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressView.setText(ctx.getResources().getString(R.string.rank_progress,
                    rank.getProgress()));
        }

        // Animate progress bar
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, progressBar.getProgress(),
                rank.getProgress());
        anim.setDuration(1000);
        progressBar.startAnimation(anim);
    }
}
