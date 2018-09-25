package fr.corenting.edcompanion.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.MainActivity;

public class ThemeUtils {

    public static boolean isDarkThemeEnabled(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(c.getString(R.string.settings_dark_theme), false);
    }

    public static void setTheme(Activity activity) {
        if (isDarkThemeEnabled(activity)) {
            if (activity instanceof MainActivity) {
                activity.setTheme(R.style.AppDarkTheme);
            } else {
                activity.setTheme(R.style.AppDarkDetailsTheme);
            }
        } else {
            if (activity instanceof MainActivity) {
                activity.setTheme(R.style.AppTheme);
            } else {
                activity.setTheme(R.style.AppDetailsTheme);
            }
        }
    }
}