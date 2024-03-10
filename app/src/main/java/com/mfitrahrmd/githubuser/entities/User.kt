package com.mfitrahrmd.githubuser.entities

import java.util.Date

data class User(
    val gistsUrl: String? = null,

    val reposUrl: String? = null,

    val twitterUsername: String? = null,

    val bio: String? = null,

    val createdAt: Date? = null,

    val username: String,

    val type: String? = null,

    val blog: String? = null,

    val subscriptionsUrl: String? = null,

    val updatedAt: Date? = null,

    val siteAdmin: Boolean? = null,

    val company: String? = null,

    val id: Int,

    val publicRepos: Int? = null,

    val gravatarId: String? = null,

    val email: String? = null,

    val organizationsUrl: String? = null,

    val hireable: Boolean? = null,

    val starredUrl: String? = null,

    val publicGists: Int? = null,

    val url: String,

    val receivedEventsUrl: String? = null,

    val avatarUrl: String,

    val eventsUrl: String? = null,

    val htmlUrl: String? = null,

    val following: Following,

    val followers: Followers,

    val name: String? = null,

    val location: String? = null,

    val nodeId: String? = null,

    val favorite: Favorite

) {
    data class Following(
        val count: Int?,
        val url: String?
    )

    data class Followers(
        val count: Int?,
        val url: String?
    )

    data class Favorite(
        val `is`: Boolean,
        val addedAt: Date? = null
    )
}