package fr.corenting.edcompanion.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.corenting.edcompanion.R;

public class RankViewUtils {
    public static void setContent(Context ctx, View rootView, int logoId, String name, int progress) {
        ImageView logoView = (ImageView) rootView.findViewById(R.id.itemImageView);
        TextView titleView = (TextView) rootView.findViewById(R.id.titleTextView);
        TextView progressView = (TextView) rootView.findViewById(R.id.progressTextView);
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        logoView.setImageResource(logoId);
        titleView.setText(name);
        progressView.setText(ctx.getResources().getString(R.string.rank_progress, progress));
        progressBar.setProgress(progress);
    }
}
