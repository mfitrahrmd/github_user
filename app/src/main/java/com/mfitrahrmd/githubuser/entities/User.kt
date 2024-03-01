package com.mfitrahrmd.githubuser.entities

import java.util.Date

data class User(
    val gistsUrl: String,

    val reposUrl: String,

    val twitterUsername: String? = null,

    val bio: String? = null,

    val createdAt: Date? = null,

    val username: String,

    val type: String,

    val blog: String? = null,

    val subscriptionsUrl: String,

    val updatedAt: Date? = null,

    val siteAdmin: Boolean,

    val company: String? = null,

    val id: Int,

    val publicRepos: Int? = null,

    val gravatarId: String?,

    val email: String? = null,

    val organizationsUrl: String,

    val hireable: Boolean? = null,

    val starredUrl: String,

    val publicGists: Int? = null,

    val url: String,

    val receivedEventsUrl: String,

    val avatarUrl: String,

    val eventsUrl: String,

    val htmlUrl: String,

    val following: Following,

    val followers: Followers,

    val name: String? = null,

    val location: String? = null,

    val nodeId: String,

    val favorite: Favorite

) {
    data class Following(
        val count: Int? = null,
        val url: String
    )

    data class Followers(
        val count: Int? = null,
        val url: String
    )

    data class Favorite(
        val `is`: Boolean,
        val addedAt: Date? = null
    )
}