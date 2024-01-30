package com.mfitrahrmd.githubuser.models

data class User(
    val gistsUrl: String,

    val reposUrl: String,

    val followingUrl: String,

    val twitterUsername: String? = null,

    val bio: String? = null,

    val createdAt: String? = null,

    val login: String,

    val type: String,

    val blog: String? = null,

    val subscriptionsUrl: String,

    val updatedAt: String? = null,

    val siteAdmin: Boolean,

    val company: String? = null,

    val id: Int,

    val publicRepos: Int? = null,

    val gravatarId: String?,

    val email: String? = null,

    val organizationsUrl: String,

    val hireable: Boolean? = null,

    val starredUrl: String,

    val followersUrl: String,

    val publicGists: Int? = null,

    val url: String,

    val receivedEventsUrl: String,

    val followers: Int? = null,

    val avatarUrl: String,

    val eventsUrl: String,

    val htmlUrl: String,

    val following: Int? = null,

    val name: String? = null,

    val location: String? = null,

    val nodeId: String

)