package fr.corenting.edcompanion.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.orhanobut.hawk.Hawk;

import fr.corenting.edcompanion.R;

public class SettingsUtils {

    public static String getCommanderName(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getString(c.getString(R.string.settings_cmdr_username), "");
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


    public static String getString(Context c, String key) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getString(key, "");
    }

    public static String getSecureString(Context c, String key) {
        Hawk.init(c).build();
        return Hawk.get(key, "");
    }

    public static void setString(Context c, String key, String value) {
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferenceManager.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
