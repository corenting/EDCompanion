package fr.corenting.edcompanion.utils;


import android.app.Activity;
import android.support.design.widget.Snackbar;

import fr.corenting.edcompanion.R;

public class NotificationsUtils {
    public static void displayErrorSnackbar(Activity activity)
    {
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content),
                        R.string.download_error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
