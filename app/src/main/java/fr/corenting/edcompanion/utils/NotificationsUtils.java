package fr.corenting.edcompanion.utils;


import android.app.Activity;
import android.support.design.widget.Snackbar;

import fr.corenting.edcompanion.R;

public class NotificationsUtils {

    public static void displayDownloadErrorSnackbar(Activity activity)
    {
        displaySnackbar(activity, activity.getString(R.string.download_error));
    }

    public static void displaySnackbar(Activity activity, String message)
    {
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content),
                        message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
