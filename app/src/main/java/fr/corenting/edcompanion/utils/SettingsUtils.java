package fr.corenting.edcompanion.utils;


import android.content.Context;
import android.preference.PreferenceManager;

import fr.corenting.edcompanion.R;

public class SettingsUtils {

    public static String getCommanderName(Context c)
    {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getString(c.getString(R.string.settings_cmdr), "");
    }
}
