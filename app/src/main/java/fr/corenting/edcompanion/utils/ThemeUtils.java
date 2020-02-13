package fr.corenting.edcompanion.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.MainActivity;

public class ThemeUtils {

    public static boolean isDarkThemeEnabled(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(c.getString(R.string.settings_dark_theme), false);
    }

    public static void setTheme(Activity activity) {
        if (activity instanceof MainActivity) {
            activity.setTheme(R.style.AppTheme);
        } else {
            activity.setTheme(R.style.AppDetailsTheme);
        }
        if (isDarkThemeEnabled(activity)) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}