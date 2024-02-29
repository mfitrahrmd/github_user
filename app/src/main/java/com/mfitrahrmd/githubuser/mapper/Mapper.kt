package com.mfitrahrmd.githubuser.mapper

import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.entities.network.NetworkUser

interface Mapper<I, O> {
    fun map(input: I): O
}

interface ListMapper<I, O> : Mapper<List<I>, List<O>>

open class ListMapperImpl<I, O>(private val _mapper: Mapper<I, O>) : ListMapper<I, O> {
    override fun map(input: List<I>): List<O> {
        return input.map { _mapper.map(it) }
    }
}

object NetworkUserToUser : Mapper<NetworkUser, User> {
    override fun map(input: NetworkUser): User {
        return User(
            id = input.id,
            login = input.login,
            name = input.name,
            blog = input.blog,
            avatarUrl = input.avatarUrl,
            eventsUrl = input.eventsUrl,
            followingUrl = input.followingUrl,
            followersUrl = input.followersUrl,
            gistsUrl = input.gistsUrl,
            htmlUrl = input.htmlUrl,
            nodeId = input.nodeId,
            type = input.type,
            organizationsUrl = input.organizationsUrl,
            url = input.url,
            siteAdmin = input.siteAdmin,
            starredUrl = input.starredUrl,
            reposUrl = input.reposUrl,
            receivedEventsUrl = input.receivedEventsUrl,
            subscriptionsUrl = input.subscriptionsUrl,
            gravatarId = input.gravatarId,
            followers = input.followers,
            following = input.following,
            twitterUsername = input.twitterUsername,
            company = input.company,
            updatedAt = input.updatedAt,
            createdAt = input.createdAt,
            bio = input.bio,
            hireable = input.hireable,
            location = input.location,
            email = input.email,
            publicGists = input.publicGists,
            publicRepos = input.publicRepos,
        )
    }
}

object ListNetworkUserToListUser : ListMapperImpl<NetworkUser, User>(NetworkUserToUser)

fun NetworkUser.toUser() = NetworkUserToUser.map(this)

fun List<NetworkUser>.toUser(): List<User> = ListNetworkUserToListUser.map(this)