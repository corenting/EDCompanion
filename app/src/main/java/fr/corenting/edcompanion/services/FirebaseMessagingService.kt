package fr.corenting.edcompanion.services

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.activities.MainActivity
import fr.corenting.edcompanion.utils.NotificationsUtils
import java.lang.Integer.parseInt

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
        val goalTitle = remoteMessage.data["title"]
        val currentTier = parseInt(remoteMessage.data["current_tier"]!!)

        val channelId = NotificationsUtils.getChannelNameAndDescription(this, type).first

        // Intent to launch app
        var intentFlags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intentFlags = intentFlags or PendingIntent.FLAG_IMMUTABLE
        }
        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            intent, intentFlags
        )

        val mBuilder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(goalTitle)
            .setAutoCancel(true)
            .setContentText(NotificationsUtils.getNotificationContent(this, type, currentTier))
            .setContentIntent(contentIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(goalTitle.hashCode(), mBuilder.build())
        }
    }
}
