package com.mfitrahrmd.githubuser.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {
    fun toDate(date: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        return format.parse(date)
    }
}