package fr.corenting.edcompanion.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fr.corenting.edcompanion.R;

public class SettingsUtils {

    public static String getCommanderName(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getString(c.getString(R.string.settings_cmdr), "");
    }

    public static boolean getBoolean(Context c, String key) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getBoolean(key, false);
    }

    public static int getInt(Context c, String key) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getInt(key, -1);
    }

    public static void setInt(Context c, String key, int value) {
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferenceManager.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
