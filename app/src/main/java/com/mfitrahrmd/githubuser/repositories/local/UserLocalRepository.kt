package com.mfitrahrmd.githubuser.repositories.local

import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository

class UserLocalRepository : UserRepository() {
    private val _user = User(
        name = "Muhamad Fitrah Ramadhan",
        publicRepos = 1,
        publicGists = 1,
        email = "mfitrahrmd@gmil.com",
        location = "Indonesia",
        hireable = true,
        blog = "blog.mfitrahrmd.me",
        bio = "Studying",
        avatarUrl = "https://mfitrahrmd.me/avatar.jpg",
        eventsUrl = "https://mfitrahrmd.me/events",
        followersUrl = "https://mfitrahrmd.me/followers",
        followingUrl = "https://mfitrahrmd.me/following",
        gistsUrl = "https://mfitrahrmd.me/gists",
        htmlUrl = "https://mfitrahrmd.me/html",
        login = "mfitrhrmd",
        nodeId = "1",
        url = "https://mfitrahrmd.me",
        organizationsUrl = "https://mfitrahrmd.me/orion",
        type = "User",
        siteAdmin = false,
        starredUrl = "https://mfitrahrmd.me/starred",
        reposUrl = "https://mfitrahrmd.me/repos",
        createdAt = "1/1/2021",
        updatedAt = "1/1/2024",
        receivedEventsUrl = "https://mfitrahrmd.me/received-events",
        company = "Orion",
        subscriptionsUrl = "https://mfitrahrmd.me/subscriptions",
        gravatarId = "https://mfitrahrmd.me/gravatar",
        twitterUsername = "mfitrahrmd",
        following = 1,
        followers = 1,
        id = 1
    )

    override suspend fun searchUsers(query: String): List<User>? {
        return listOf(_user)
    }

    override suspend fun findUserByUsername(username: String): User? {
        return _user
    }

    override suspend fun listUserFollowers(username: String): List<User>? {
        return listOf(_user)
    }

    override suspend fun listUserFollowing(username: String): List<User>? {
        return listOf(_user)
    }
}