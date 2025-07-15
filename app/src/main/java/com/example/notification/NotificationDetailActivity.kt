package com.example.notification

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notification.databinding.ActivityNotificationDetailBinding

class NotificationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val content = intent.getStringExtra(EXTRA_NOTIFICATION_CONTENT)
        binding.Content.text = content
        val title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE)
        binding.Title.text = title
    }
    companion object {
        const val EXTRA_NOTIFICATION_CONTENT = "com.example.notification.EXTRA_NOTIFICATION_CONTENT"
        const val EXTRA_NOTIFICATION_TITLE = "com.example.notification.EXTRA_NOTIFICATION_TITLE"

    }
}