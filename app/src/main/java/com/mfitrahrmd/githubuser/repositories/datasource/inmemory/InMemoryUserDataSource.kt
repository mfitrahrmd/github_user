package com.mfitrahrmd.githubuser.repositories.datasource.inmemory

import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.repositories.datasource.UserDataSource

private val _user = RemoteUser(
    id = 0,
    login = "user0",
    name = "user0",
    email = "user0@gmail.com",
    bio = "lorem ipsum",
    location = "",
    followingUrl = "",
    followersUrl = "",
    createdAt = "",
    updatedAt = "",
    following = 0,
    followers = 0,
    blog = "",
    avatarUrl = "",
    eventsUrl = "",
    gistsUrl = "",
    htmlUrl = "",
    nodeId = "",
    type = "",
    url = "",
    organizationsUrl = "",
    siteAdmin = false,
    starredUrl = "",
    reposUrl = "",
    receivedEventsUrl = "",
    subscriptionsUrl = "",
    gravatarId = "",
    twitterUsername = "",
    company = "",
    hireable = null,
    publicGists = 0,
    publicRepos = 0
)


class InMemoryUserDataSource private constructor() : UserDataSource {
    private val _users: List<RemoteUser> = List(100) { i ->
        _user.copy(
            id = i,
            login = "user$i",
            name = "user$i",
            email = "user$i@gmail.com",
            location = "indonesia",
            following = 100,
            followers = 100
        )
    }

    override suspend fun searchUsers(
        query: String, page: Int?, perPage: Int?
    ): List<RemoteUser> {
        val p = (page ?: 1) - 1
        val pp = perPage ?: 20
        val start = if (p == 0) 0 else p * pp
        val end = start + pp - 1
        var filtered = if (query.startsWith("location:")) {
            val location = query.split(":")[1]
            _users.filter {
                it.location.equals(location)
            }
        } else {
            _users.filter {
                it.login.contains(query)
            }
        }

        return if (filtered.size > pp) filtered.slice(start..end) else filtered
    }

    override suspend fun findUserByUsername(username: String): RemoteUser? {
        return _users.firstOrNull {
            it.login.contains(username)
        }
    }

    override suspend fun listUserFollowers(
        username: String, page: Int?, perPage: Int?
    ): List<RemoteUser> {
        val p = (page ?: 1) - 1
        val pp = perPage ?: 20
        val start = if (p == 0) 0 else p * pp
        val end = start + pp - 1

        return if (start > _users.size || end > _users.size) {
            emptyList()
        } else {
            if (pp > _users.size) _users else _users.slice(start..end)
        }
    }

    override suspend fun listUserFollowing(
        username: String, page: Int?, perPage: Int?
    ): List<RemoteUser> {
        val p = (page ?: 1) - 1
        val pp = perPage ?: 20
        val start = if (p == 0) 0 else p * pp
        val end = start + pp - 1

        return if (start > _users.size || end > _users.size) {
            emptyList()
        } else {
            if (pp > _users.size) _users else _users.slice(start..end)
        }
    }

    companion object {
        @Volatile
        private var _INSTANCE: InMemoryUserDataSource? = null

        fun getInstance(): InMemoryUserDataSource {
            return _INSTANCE ?: synchronized(this) {
                val instance = InMemoryUserDataSource()
                _INSTANCE = instance

                instance
            }
        }
    }
}