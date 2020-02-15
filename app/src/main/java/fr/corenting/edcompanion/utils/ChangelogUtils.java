package fr.corenting.edcompanion.utils;

import android.content.Context;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;

public class ChangelogUtils {
    private static final String changelogKey = "changelog_version";

    public static void ShowChangelog(Context c) {
        // Get app versions (latest shown and current)
        int latestShown = SettingsUtils.getInt(c, changelogKey);
        int current = BuildConfig.VERSION_CODE;

        // Don't show if it has already be done
        if (latestShown >= current) {
            return;
        }

        // Else show dialog
        AlertDialog dialog = new MaterialAlertDialogBuilder(c)
                .setTitle(R.string.changelog_title)
                .setMessage(Html.fromHtml(c.getString(R.string.changelog_message)))
                .setPositiveButton(android.R.string.ok,
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        dialog.show();

        SettingsUtils.setInt(c, changelogKey, current);
    }
}
