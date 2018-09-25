package fr.corenting.edcompanion.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import fr.corenting.edcompanion.R;

public class ThemeUtils {
    public static boolean isDarkTheme(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(c.getString(R.string.settings_dark_theme), false);
    }

    public static int getDarkThemeValue(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        boolean enabled = sharedPreferences.getBoolean(c.getString(R.string.settings_dark_theme), false);
        return enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
    }

    public static void setToolbarColor(Activity activity, Toolbar toolbar) {
        int baseColor;
        if (isDarkTheme(activity)) {
            baseColor = activity.getResources().getColor(R.color.darkPrimary);
        } else {
            baseColor = activity.getResources().getColor(R.color.colorPrimary);
        }
        toolbar.setBackgroundColor(baseColor);
    }
}