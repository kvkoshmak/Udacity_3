package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val name = intent.getStringExtra("name")
        val status = intent.getStringExtra("status")
        statusInput.text = status
        fileNameInput.text = name
//        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()

        if (status == "Failed"){
            statusInput.setTextColor(Color.RED)
        } else {
            statusInput.setTextColor(Color.GREEN)
        }

        val notificationManager = ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
        ) as NotificationManager
        //Send the data with notification
        notificationManager.cancelAll()

    }

    fun previous(view:View){
        onBackPressed()
    }
}
