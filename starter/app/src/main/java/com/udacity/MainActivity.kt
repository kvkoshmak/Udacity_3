package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var firstRepoDownloadID : Long = 0
    private var secondRepoDownloadID : Long = 0
    private var thirdRepoDownloadID : Long = 0
    private var name :String = ""
    private var link: String? = null

//    private lateinit var notificationManager: NotificationManager
//    private lateinit var pendingIntent: PendingIntent
//    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (link != null) {
                download()
//                Toast.makeText(this, link, Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, R.string.no_link, Toast.LENGTH_SHORT).show()
        }

        radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_1 -> link = URL_GL
                R.id.radio_button_2 -> link = URL_UD
                R.id.radio_button_3 -> link = URL_RET
            }
        }

        createChannel(
                getString(R.string.channel_id),
                getString(R.string.channel_name)
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            when (id) {
                firstRepoDownloadID->  name = getString(R.string.radio_1)
                secondRepoDownloadID -> name = getString(R.string.radio_2)
                thirdRepoDownloadID -> name = getString(R.string.radio_3)
            }
            val query = DownloadManager.Query().setFilterById(id)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(query)
            cursor.moveToFirst()
            val status = when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> "Success"
                else -> "Failed"
            }

            val notificationManager = ContextCompat.getSystemService(
                    context!!,
                    NotificationManager::class.java
            ) as NotificationManager
            //Send the data with notification
            notificationManager.sendNotification(
                    "The Project 3 repository is downloaded",
                    name,
                    status,
                    context
            )
            
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(link))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        when (link ){
            URL_GL -> firstRepoDownloadID = downloadManager.enqueue(request)
            URL_UD -> secondRepoDownloadID = downloadManager.enqueue(request)
            URL_RET -> thirdRepoDownloadID = downloadManager.enqueue(request)
        }

    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            )
                    .apply {
                        setShowBadge(false)
                    }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.channel_name)

            val notificationManager = this.getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    companion object {
        private const val URL_UD =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GL = "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL_RET = "https://github.com/square/retrofit/archive/master.zip"
    }

}
