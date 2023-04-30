package com.example.dm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dm.R
import com.example.dm.presentation.activity.ChatActivity
import com.example.dm.presentation.activity.MainActivity
import com.example.dm.utils.ConstUtils.channelId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingServices: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)

        val pendingIntent = PendingIntent.getActivities(
            this,
            0,
            arrayOf(intent), PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["body"])
            .setSmallIcon(R.drawable.round_notifications_24)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.data["body"]))
            .setContentIntent(pendingIntent)
            .build()


        manager.notify(Random().nextInt(), notification)
    }

    fun createNotificationChannel(manager: NotificationManager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "direct message",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "new test message"
            channel.enableLights(true)
            manager.createNotificationChannel(channel)
        } else {

        }




    }
}