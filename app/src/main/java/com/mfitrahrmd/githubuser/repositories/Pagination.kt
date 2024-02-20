package com.mfitrahrmd.githubuser.repositories

data class Pagination<T>(val first: Int?, val last: Int?, val previous: Int?, val next: Int?, val data: T?)
