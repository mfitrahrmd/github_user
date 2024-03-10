package com.mfitrahrmd.githubuser.mapper

interface NetworkToLocal<LocalEntity> {
    fun toLocalEntity(): LocalEntity
}