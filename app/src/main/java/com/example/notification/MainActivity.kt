package com.example.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notification.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var notificationId = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var builder: NotificationCompat.Builder

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        createNotificationChannel()
        binding.btnCreateNotification.setOnClickListener {
            createNotification()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                sendNotification()
            } else {
                Snackbar.make(
                    binding.main,
                    getString(R.string.txt_permission_denied),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun sendNotification() {
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId++, builder.build())
        }
    }


    companion object {
        const val CHANNEL_ID = "channel_id2"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createNotification() {
        val bitmapNull: Bitmap? = null
        val iconBitmap = BitmapFactory.decodeResource(resources, R.drawable.img)
        val notifiContent = getString(R.string.notification_content) + " " + Utils.getCurrentTime()
        val intent = Intent(this, NotificationDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(NotificationDetailActivity.EXTRA_NOTIFICATION_CONTENT, notifiContent)
            putExtra(
                NotificationDetailActivity.EXTRA_NOTIFICATION_TITLE,
                getString(R.string.notification_title)
            )
        }
        val pendingIntent =
            PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_IMMUTABLE)
        builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_svgrepo_com)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(notifiContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setLargeIcon(iconBitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(iconBitmap).bigLargeIcon(bitmapNull)
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(CHANNEL_ID)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)
            .setGroupSummary(true)
            .addAction(R.drawable.notification_svgrepo_com, "Download", pendingIntent)
            .addAction(R.drawable.ic_launcher_background, "Detail", pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    baseContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    Snackbar.make(
                        binding.main,
                        getString(R.string.notification_permission_require),
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                } else {
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
                return
            }
            notify(notificationId++, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText =
                getString(R.string.channel_description) + " " + Utils.getCurrentTime()
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setShowBadge(true)

            }
            val notificationManager: NotificationManager = ContextCompat.getSystemService(
                baseContext,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}