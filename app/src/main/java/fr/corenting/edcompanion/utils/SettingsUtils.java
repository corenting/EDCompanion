package fr.corenting.edcompanion.utils;


import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

import fr.corenting.edcompanion.R;

public class SettingsUtils {

    public static String getEdsmApiKey(Context c)
    {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getString(c.getString(R.string.settings_edsm_key), "");
    }

    public static String getCommanderName(Context c)
    {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getString(c.getString(R.string.settings_cmdr), "");
    }

    public static boolean hasValidCmdrParameters(Context c)
    {
        return !getEdsmApiKey(c).equals("") && !getCommanderName(c).equals("");
    }
}
