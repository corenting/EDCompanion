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

    public static int getCombatLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_combat;
            case 2:
                return R.drawable.rank_2_combat;
            case 3:
                return R.drawable.rank_3_combat;
            case 4:
                return R.drawable.rank_4_combat;
            case 5:
                return R.drawable.rank_5_combat;
            case 6:
                return R.drawable.rank_6_combat;
            case 7:
                return R.drawable.rank_7_combat;
            case 8:
                return R.drawable.rank_8_combat;
            case 9:
                return R.drawable.rank_9_combat;
        }
    }

    public static int getTradeLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_trading;
            case 2:
                return R.drawable.rank_2_trading;
            case 3:
                return R.drawable.rank_3_trading;
            case 4:
                return R.drawable.rank_4_trading;
            case 5:
                return R.drawable.rank_5_trading;
            case 6:
                return R.drawable.rank_6_trading;
            case 7:
                return R.drawable.rank_7_trading;
            case 8:
                return R.drawable.rank_8_trading;
            case 9:
                return R.drawable.rank_9_trading;
        }
    }

    public static int getExplorationLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_exploration;
            case 2:
                return R.drawable.rank_2_exploration;
            case 3:
                return R.drawable.rank_3_exploration;
            case 4:
                return R.drawable.rank_4_exploration;
            case 5:
                return R.drawable.rank_5_exploration;
            case 6:
                return R.drawable.rank_6_exploration;
            case 7:
                return R.drawable.rank_7_exploration;
            case 8:
                return R.drawable.rank_8_exploration;
            case 9:
                return R.drawable.rank_9_exploration;
        }
    }

    public static int getCqcLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_cqc;
            case 2:
                return R.drawable.rank_2_cqc;
            case 3:
                return R.drawable.rank_3_cqc;
            case 4:
                return R.drawable.rank_4_cqc;
            case 5:
                return R.drawable.rank_5_cqc;
            case 6:
                return R.drawable.rank_6_cqc;
            case 7:
                return R.drawable.rank_7_cqc;
            case 8:
                return R.drawable.rank_8_cqc;
            case 9:
                return R.drawable.rank_9_cqc;
        }
    }
}
