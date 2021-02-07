package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0


fun NotificationManager.sendNotification(messageBody: String, name: String, status: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("name",name)
    contentIntent.putExtra("status",status)


    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notImage = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_assistant_black_24dp
    )
    val bigPicStyle = NotificationCompat.InboxStyle()
//            .bigPicture(notImage)
//            .bigLargeIcon(null)

    // Build the notification
    val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.channel_id)
    )

            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext
                    .getString(R.string.notification_title))
            .setContentText(messageBody)

            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

            .setStyle(bigPicStyle)
            .setLargeIcon(notImage)

            .addAction(
                    R.drawable.ic_assistant_black_24dp,
                    "DetailActivity",
                    contentPendingIntent
            )

            .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
