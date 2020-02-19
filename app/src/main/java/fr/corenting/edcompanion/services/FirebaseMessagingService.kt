package fr.corenting.edcompanion.services

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonParser
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.activities.MainActivity
import fr.corenting.edcompanion.utils.NotificationsUtils

class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        NotificationsUtils.refreshPushSubscriptions(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Ignore empty messages
        if (remoteMessage.data.isEmpty()) {
            return
        }

        try {
            handlePushMessage(remoteMessage)
        } catch (e: Exception) {
            Log.i("Firebase", "${e.message} when handling push message")
        }

    }

    private fun handlePushMessage(remoteMessage: RemoteMessage) {
        val type = remoteMessage.from?.substring(8)
        val json = JsonParser.parseString(remoteMessage.data["goal"]).asJsonObject
        val goalTitle = json.get("title").asString
        val currentTier = json.get("tier_progress").asJsonObject.get("current").asInt

        val channelId = NotificationsUtils.createNotificationChannel(this, type)

        // Intent to launch app
        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val largeIcon = BitmapFactory.decodeResource(resources,
                R.drawable.ic_notification)

        val mBuilder = NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(goalTitle)
                .setAutoCancel(true)
                .setContentText(NotificationsUtils.getNotificationContent(this, type, currentTier))
                .setContentIntent(contentIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(goalTitle.hashCode(), mBuilder.build())
    }
}
