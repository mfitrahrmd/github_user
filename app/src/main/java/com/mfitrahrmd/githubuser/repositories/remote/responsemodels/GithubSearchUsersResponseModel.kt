package com.mfitrahrmd.githubuser.repositories.remote.responsemodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubSearchUsersResponseModel(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<User>
) : Parcelable

@Parcelize
data class User(

    @field:SerializedName("gists_url")
    val gistsUrl: String,

    @field:SerializedName("repos_url")
    val reposUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("starred_url")
    val starredUrl: String,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("subscriptions_url")
    val subscriptionsUrl: String,

    @field:SerializedName("score")
    val score: Float,

    @field:SerializedName("received_events_url")
    val receivedEventsUrl: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("events_url")
    val eventsUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("site_admin")
    val siteAdmin: Boolean,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("gravatar_id")
    val gravatarId: String?,

    @field:SerializedName("node_id")
    val nodeId: String,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String
) : Parcelable

fun GithubSearchUsersResponseModel.toUser(): List<com.mfitrahrmd.githubuser.entities.User> {
    return this.items.map {
        com.mfitrahrmd.githubuser.entities.User(
            url = it.url,
            avatarUrl = it.avatarUrl,
            eventsUrl = it.eventsUrl,
            followersUrl = it.followersUrl,
            followingUrl = it.followingUrl,
            gistsUrl = it.gistsUrl,
            htmlUrl = it.htmlUrl,
            receivedEventsUrl = it.receivedEventsUrl,
            organizationsUrl = it.organizationsUrl,
            subscriptionsUrl = it.subscriptionsUrl,
            starredUrl = it.starredUrl,
            reposUrl = it.reposUrl,
            id = it.id,
            nodeId = it.nodeId,
            type = it.type,
            login = it.login,
            siteAdmin = it.siteAdmin,
            gravatarId = it.gravatarId,
        )
    }
}