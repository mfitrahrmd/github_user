package com.mfitrahrmd.githubuser.repositories.remote.responsemodels

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mfitrahrmd.githubuser.models.User

@Parcelize
data class GithubFindUserByUsernameResponseModel(

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
) : Parcelable

fun GithubFindUserByUsernameResponseModel.toUser(): User {
	return User(
		id = this.id,
		followers = this.followers,
		following = this.following,
		twitterUsername = this.twitterUsername,
		gravatarId = this.gravatarId,
		company = this.company,
		createdAt = this.createdAt,
		updatedAt = this.updatedAt,
		reposUrl = this.reposUrl,
		starredUrl = this.starredUrl,
		siteAdmin = this.siteAdmin,
		type = this.type,
		subscriptionsUrl = this.subscriptionsUrl,
		url = this.url,
		receivedEventsUrl = this.receivedEventsUrl,
		organizationsUrl = this.organizationsUrl,
		nodeId = this.nodeId,
		login = this.login,
		htmlUrl = this.htmlUrl,
		gistsUrl = this.gistsUrl,
		followingUrl = this.followingUrl,
		followersUrl = this.followersUrl,
		eventsUrl = this.eventsUrl,
		avatarUrl = this.avatarUrl,
		bio = this.bio,
		blog = this.blog,
		email = this.email,
		hireable = this.hireable,
		location = this.location,
		name = this.name,
		publicGists = this.publicGists,
		publicRepos = this.publicRepos
	)
}