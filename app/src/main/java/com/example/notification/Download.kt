package com.example.notification

import android.Manifest
import android.app.DownloadManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notification.databinding.ActivityDownloadBinding
import com.google.android.material.snackbar.Snackbar

class Download : AppCompatActivity() {
    private var videoId: Int = 0
    private lateinit var binding: ActivityDownloadBinding
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            downloadFile()
        } else {
            Snackbar.make(binding.main, "Permission denied", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnDownload.setOnClickListener {
            checkPermissions()
        }

    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }
        downloadFile()

    }

    private fun downloadFile() {
        val uri = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4\n".toUri()
        val request = DownloadManager.Request(uri)
        val subPath = "${TARGET_NAME}${TYPE_FILE}"
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Downloading ....")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subPath)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    companion object {
        private const val HOST_NAME = "https://braniumacademy.net"
        const val TARGET_NAME = "install_netbeans"
        const val TYPE_FILE = ".mp4"
        const val DOWNLOAD_URL = "${HOST_NAME}/resources/videos/java/${TARGET_NAME}${TYPE_FILE}"
    }
}