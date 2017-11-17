package fr.corenting.edcompanion.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.MainActivity;
import fr.corenting.edcompanion.utils.NotificationsUtils;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Ignore empty messages
        if (remoteMessage.getData().size() <= 0) {
            return;
        }

        try {
            handlePushMessage(remoteMessage);
        } catch (Exception e) {
            Log.i("Firebase", e.getMessage() + " when handling push message");
        }
    }

    private void handlePushMessage(RemoteMessage remoteMessage) {
        String type = remoteMessage.getFrom().substring(8);
        JsonObject json = new JsonParser().parse(remoteMessage.getData().get("goal")).getAsJsonObject();
        String goalTitle = json.get("title").getAsString();
        int currentTier = json.get("tier_progress").getAsJsonObject().get("current").getAsInt();

        String channelId = NotificationsUtils.createNotificationChannel(this, type);

        // Intent to launch app
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_notification);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setLargeIcon(largeIcon)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(goalTitle)
                        .setAutoCancel(true)
                        .setContentText(NotificationsUtils.getNotificationContent(this, type, currentTier))
                        .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(goalTitle.hashCode(), mBuilder.build());
    }
}
