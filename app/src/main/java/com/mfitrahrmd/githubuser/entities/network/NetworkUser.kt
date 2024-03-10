package com.mfitrahrmd.githubuser.entities.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.mapper.NetworkToLocal
import com.mfitrahrmd.githubuser.utils.DateFormat
import kotlinx.parcelize.Parcelize

@Parcelize
data class NetworkUser(

    @field:SerializedName("gists_url")
    val gistsUrl: String,

    @field:SerializedName("repos_url")
    val reposUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("twitter_username")
    val twitterUsername: String?,

    @field:SerializedName("bio")
    val bio: String?,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("blog")
    val blog: String?,

    @field:SerializedName("subscriptions_url")
    val subscriptionsUrl: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("site_admin")
    val siteAdmin: Boolean,

    @field:SerializedName("company")
    val company: String?,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("gravatar_id")
    val gravatarId: String?,

    @field:SerializedName("email")
    val email: String?,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String,

    @field:SerializedName("hireable")
    val hireable: Boolean?,

    @field:SerializedName("starred_url")
    val starredUrl: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("public_gists")
    val publicGists: Int,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("received_events_url")
    val receivedEventsUrl: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("events_url")
    val eventsUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("location")
    val location: String?,

    @field:SerializedName("node_id")
    val nodeId: String
) : Parcelable, NetworkToLocal<DBPopularUser> {
    override fun toLocalEntity(): DBPopularUser {
        return DBPopularUser(
            id = this.id,
            publicRepos = this.publicRepos,
            publicGists = this.publicGists,
            email = this.email,
            location = this.location,
            hireable = this.hireable,
            bio = this.bio,
            company = this.company,
            twitterUsername = this.twitterUsername,
            gravatarId = this.gravatarId,
            subscriptionsUrl = this.subscriptionsUrl,
            receivedEventsUrl = this.receivedEventsUrl,
            reposUrl = this.reposUrl,
            starredUrl = this.starredUrl,
            siteAdmin = this.siteAdmin,
            url = this.url,
            organizationsUrl = this.organizationsUrl,
            type = this.type,
            nodeId = this.nodeId,
            htmlUrl = this.htmlUrl,
            gistsUrl = this.gistsUrl,
            eventsUrl = this.eventsUrl,
            avatarUrl = this.avatarUrl,
            blog = this.blog,
            name = this.name,
            followers = this.followers,
            following = this.following,
            login = this.login,
            followingUrl = this.followingUrl,
            followersUrl = this.followersUrl,
            updatedAt = DateFormat.toDate(this.updatedAt),
            createdAt = DateFormat.toDate(this.createdAt)
        )
    }
}