package com.mfitrahrmd.githubuser.entities.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = DBUserFollowing.TABLE_NAME,
)
data class DBUserFollowing(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,

    val userDbId: Int? = null,

    val id: Int,

    val gistsUrl: String?,

    val reposUrl: String?,

    val followingUrl: String?,

    val twitterUsername: String?,

    val bio: String?,

    val createdAt: Date?,

    val login: String,

    val type: String?,

    val blog: String?,

    val subscriptionsUrl: String?,

    val updatedAt: Date?,

    val siteAdmin: Boolean?,

    val company: String?,

    val publicRepos: Int?,

    val gravatarId: String?,

    val email: String?,

    val organizationsUrl: String?,

    val hireable: Boolean?,

    val starredUrl: String?,

    val followersUrl: String?,

    val publicGists: Int?,

    val url: String,

    val receivedEventsUrl: String?,

    val followers: Int?,

    val avatarUrl: String,

    val eventsUrl: String?,

    val htmlUrl: String?,

    val following: Int?,

    val name: String?,

    val location: String?,

    val nodeId: String?
) {
    companion object {
        const val TABLE_NAME = "user_following"
    }
}