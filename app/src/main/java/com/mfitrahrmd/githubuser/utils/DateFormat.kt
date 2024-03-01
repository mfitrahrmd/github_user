package com.mfitrahrmd.githubuser.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

object DateFormat {
    fun toDate(date: String?): Date? {
        if (date.isNullOrEmpty() || date.isBlank()) return null

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val x = formatter.parse(date)
        if (x != null) {
            Log.d("DATE", x.toString())
        }

        return x
    }
}