package fr.corenting.edcompanion.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

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

        int darkColor, baseColor;
        if (isDarkTheme(activity)) {
            baseColor = activity.getResources().getColor(R.color.darkPrimary);
            darkColor = activity.getResources().getColor(R.color.darkPrimaryDark);
        } else {
            baseColor = activity.getResources().getColor(R.color.colorPrimary);
            darkColor = activity.getResources().getColor(R.color.colorPrimaryDark);
        }
        toolbar.setBackgroundColor(baseColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(darkColor);
        }
    }
}