package fr.corenting.edcompanion.utils

import android.content.Context
import androidx.core.text.HtmlCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.corenting.edcompanion.BuildConfig
import fr.corenting.edcompanion.R

object ChangelogUtils {
    private const val changelogKey = "changelog_version"

    fun showChangelog(c: Context) {
        // Get app versions (latest shown and current)
        val latestShown = SettingsUtils.getInt(c, changelogKey)
        val current = BuildConfig.VERSION_CODE

        // Don't show if it has already be done
        if (latestShown >= current) {
            return
        }

        // Else show dialog
        val dialog = MaterialAlertDialogBuilder(c)
                .setTitle(R.string.changelog_title)
                .setMessage(HtmlCompat.fromHtml(c.getString(R.string.changelog_message), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(android.R.string.ok
                ) { dialogInterface, _ -> dialogInterface.dismiss() }
                .create()
        dialog.show()

        SettingsUtils.setInt(c, changelogKey, current)
    }
}
