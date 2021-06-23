package fr.corenting.edcompanion.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import fr.corenting.edcompanion.R

object ThemeUtils {
    fun getThemeToUse(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val valuesArray = context.resources.getStringArray(R.array.settings_theme_entries)

        return when (prefs.getString(context.getString(R.string.settings_theme), valuesArray[0])) {
            valuesArray[1] -> {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            valuesArray[2] -> {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else -> {
                when {
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }
                    else -> {
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                }
            }
        }
    }

    @JvmStatic
    fun isDarkThemeEnabled(context: Context): Boolean {
        return when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> {
                val currentNightMode: Int = (context.resources.configuration.uiMode
                        and Configuration.UI_MODE_NIGHT_MASK)
                when (currentNightMode) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        true
                    }
                    else -> false
                }
            }
            else -> {
                true
            }
        }
    }
}