package com.example.notification

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Utils {
    private const val DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"

    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat(DATE_FORMAT)
    private fun getCurrentDate(): Date {
        return Date()
    }

    fun getCurrentTime(): String {
        return dateFormatter.format(getCurrentDate())
    }
}
