package com.mfitrahrmd.githubuser.utils

import com.mfitrahrmd.githubuser.repositories.WithPagination
import okhttp3.Headers

object ApiExtractor {
    fun <T> getPagination(headers: Headers, data: T?): WithPagination<T> {
        var prev: Int? = null
        var next: Int? = null
        var first: Int? = null
        var last: Int? = null
        val link = headers.get("link")
        if (!link.isNullOrBlank()) {
            link.split(",").forEach {
                if (it.contains("\"prev\"")) {
                    prev = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
                if (it.contains("\"next\"")) {
                    next = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
                if (it.contains("\"first\"")) {
                    first = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
                if (it.contains("\"last\"")) {
                    last = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
            }
        }

        return WithPagination(data, first, last, prev, next)
    }
}