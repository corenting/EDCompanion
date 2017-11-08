package fr.corenting.edcompanion.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;

public class ChangelogUtils {
    private static final String changelogKey = "changelog_version";

    public static void ShowChangelog(Context c)
    {
        // Get app versions (latest shown and current)
        int latestShown = SettingsUtils.getInt(c, changelogKey);
        int current = BuildConfig.VERSION_CODE;

        // Don't show if it has already be done
        if (latestShown >= current)
        {
            return;
        }

        // Else show dialog
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.changelog_title)
                .setMessage(Html.fromHtml(c.getString(R.string.changelog_message)))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.show();

        SettingsUtils.setInt(c, changelogKey, current);
    }
}
