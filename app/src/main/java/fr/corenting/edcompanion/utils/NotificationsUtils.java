package fr.corenting.edcompanion.utils;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.Pair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.R;

public class NotificationsUtils {

    private static final String newGoalTopic = "new_goal";
    private static final String newTierTopic = "new_tier";
    private static final String finishedGoalTopic = "finished_goal";

    public static void displayDownloadErrorSnackbar(Activity activity) {
        displaySnackbar(activity, activity.getString(R.string.download_error));
    }

    public static void displaySnackbar(Activity activity, String message) {
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content),
                        message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private static List<Pair<String, Boolean>> getNotificationsTopics(Context c) {
        List<Pair<String, Boolean>> res = new ArrayList<>();

        res.add(new Pair<>(newGoalTopic, SettingsUtils.getBoolean(c, c.getString(R.string.settings_notifications_new_goal))));
        res.add(new Pair<>(newTierTopic, SettingsUtils.getBoolean(c, c.getString(R.string.settings_notifications_new_tier))));
        res.add(new Pair<>(finishedGoalTopic, SettingsUtils.getBoolean(c, c.getString(R.string.settings_notifications_finished_goal))));

        return res;
    }

    public static void refreshPushSubscriptions(Context c) {
        // Check that Play Services are OK and that Firebase has a token
        if (notificationsNotWorking(c)) {
            return;
        }

        List<Pair<String, Boolean>> topics = NotificationsUtils.getNotificationsTopics(c);
        for (Pair<String, Boolean> topic : topics) {
            if (topic.second) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic.first);
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.first);
            }
        }
    }

    public static void refreshPushSubscription(Context c, String preferenceName, boolean enabled) {
        // Check that Play Services are OK and that Firebase has a token
        if (notificationsNotWorking(c)) {
            return;
        }

        String topic;
        if (preferenceName.equals(c.getString(R.string.settings_notifications_new_goal)))
        {
            topic = newGoalTopic;
        }
        else if (preferenceName.equals(c.getString(R.string.settings_notifications_new_tier)))
        {
            topic = newTierTopic;
        }
        else if (preferenceName.equals(c.getString(R.string.settings_notifications_finished_goal)))
        {
            topic = finishedGoalTopic;
        }
        else
        {
            return;
        }

        if (enabled) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }
    }

    private static boolean notificationsNotWorking(Context c) {
        return ((GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(c) != ConnectionResult.SUCCESS)
                || (FirebaseInstanceId.getInstance().getToken() == null));
    }

    public static String createNotificationChannel(Context c, String type) {
        Pair<String, String> nameAndDesc = getChannelNameAndDescription(c, type);

        // Ignore on Android <= O, but return channel id anyway
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return nameAndDesc.first;
        }

        NotificationManager notificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(nameAndDesc.first, nameAndDesc.first, importance);
        mChannel.setDescription(nameAndDesc.second);
        notificationManager.createNotificationChannel(mChannel);
        return nameAndDesc.first;
    }

    private static Pair<String, String> getChannelNameAndDescription(Context c, String type) {
        switch (type) {
            case newGoalTopic:
                return new Pair<>(c.getString(R.string.notifications_new_goal_channel), c.getString(R.string.notifications_new_goal_channel_description));
            case newTierTopic:
                return new Pair<>(c.getString(R.string.notifications_new_tier_channel), c.getString(R.string.notifications_new_tier_channel_description));
            default:
                return new Pair<>(c.getString(R.string.notifications_finished_goal_channel), c.getString(R.string.notifications_finished_goal_channel_description));
        }
    }

    public static String getNotificationContent(Context c, String type, int currentTier) {
        switch (type) {
            case newGoalTopic:
                return c.getString(R.string.notifications_new_goal_description);
            case newTierTopic:
                return c.getString(R.string.notifications_new_tier_description, currentTier);
            default:
                return c.getString(R.string.notifications_finished_goal_description);
        }
    }
}
