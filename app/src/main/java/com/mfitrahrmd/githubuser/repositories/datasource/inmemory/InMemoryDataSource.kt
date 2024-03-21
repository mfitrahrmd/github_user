package com.mfitrahrmd.githubuser.repositories.datasource.inmemory

import android.util.Log
import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource

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


class InMemoryDataSource private constructor() : DataSource {

    private val _following: List<RemoteUser> = List(100) { i ->
        _user.copy(
            id = i, login = "following$i", name = "following$i", email = "following$i@gmail.com"
        )
    }
    private val _followers: List<RemoteUser> = List(100) { i ->
        _user.copy(
            id = i, login = "follower$i", name = "follower$i", email = "follower$i@gmail.com"
        )
    }
    private val _users: List<RemoteUser> = List(100) { i ->
        _user.copy(
            id = i,
            login = "user$i",
            name = "user$i",
            email = "user$i@gmail.com",
            location = "indonesia",
            following = _following.size,
            followers = _followers.size
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
        val start = if (page == null || page == 0) 0 else page * (perPage ?: 20)
        val end = start + (perPage ?: 20)

        return _followers.slice(start..end)
    }

    override suspend fun listUserFollowing(
        username: String, page: Int?, perPage: Int?
    ): List<RemoteUser> {
        val p = (page ?: 1) - 1
        val pp = perPage ?: 20
        val start = if (p == 0) 0 else p * pp
        val end = start + pp - 1
        Log.d("START", start.toString())
        Log.d("END", end.toString())

        return if (start > _following.size || end > _following.size) {
            emptyList()
        } else {
            if (pp > _following.size) _following else _following.slice(start..end)
        }
    }

    companion object {
        @Volatile
        private var _INSTANCE: InMemoryDataSource? = null

        fun getInstance(): InMemoryDataSource {
            return _INSTANCE ?: synchronized(this) {
                val instance = InMemoryDataSource()
                _INSTANCE = instance

                instance
            }
        }
    }
}