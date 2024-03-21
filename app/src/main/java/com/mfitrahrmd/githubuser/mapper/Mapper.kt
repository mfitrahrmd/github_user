package com.mfitrahrmd.githubuser.mapper

import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.entities.db.DBDetailUser
import com.mfitrahrmd.githubuser.entities.db.DBFavoriteUser
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.entities.db.DBSearchUser
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowers
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowing
import com.mfitrahrmd.githubuser.entities.network.SourceUser
import com.mfitrahrmd.githubuser.utils.DateFormat

interface Mapper<I, O> {
    fun map(input: I): O
}

interface ListMapper<I, O> : Mapper<List<I>, List<O>>

open class ListMapperImpl<I, O>(private val _mapper: Mapper<I, O>) : ListMapper<I, O> {
    override fun map(input: List<I>): List<O> {
        return input.map { _mapper.map(it) }
    }
}

object NetworkUserToUser : Mapper<SourceUser, User> {
    override fun map(input: SourceUser): User {
        return User(
            id = input.id,
            username = input.login,
            name = input.name,
            blog = input.blog,
            avatarUrl = input.avatarUrl,
            eventsUrl = input.eventsUrl,
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
            followers = User.Followers(input.followers, input.followersUrl),
            following = User.Following(input.following, input.followingUrl),
            twitterUsername = input.twitterUsername,
            company = input.company,
            updatedAt = DateFormat.toDate(input.updatedAt),
            createdAt = DateFormat.toDate(input.createdAt),
            bio = input.bio,
            hireable = input.hireable,
            location = input.location,
            email = input.email,
            publicGists = input.publicGists,
            publicRepos = input.publicRepos,
            favorite = User.Favorite(
                false,
                null
            )
        )
    }
}

object ListNetworkUserToListUser : ListMapperImpl<SourceUser, User>(NetworkUserToUser)

fun SourceUser.toUser() = NetworkUserToUser.map(this)

fun List<SourceUser>.toUser(): List<User> = ListNetworkUserToListUser.map(this)

object UserToDBSearchUser : Mapper<User, DBSearchUser> {
    override fun map(input: User): DBSearchUser {
        return DBSearchUser(
            id = input.id,
            publicRepos = input.publicRepos,
            publicGists = input.publicGists,
            email = input.email,
            location = input.location,
            hireable = input.hireable,
            bio = input.bio,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            company = input.company,
            twitterUsername = input.twitterUsername,
            following = input.following.count,
            followers = input.followers.count,
            gravatarId = input.gravatarId,
            subscriptionsUrl = input.subscriptionsUrl,
            receivedEventsUrl = input.receivedEventsUrl,
            reposUrl = input.reposUrl,
            starredUrl = input.starredUrl,
            siteAdmin = input.siteAdmin,
            url = input.url,
            organizationsUrl = input.organizationsUrl,
            type = input.type,
            nodeId = input.nodeId,
            htmlUrl = input.htmlUrl,
            gistsUrl = input.gistsUrl,
            followersUrl = input.followers.url,
            followingUrl = input.following.url,
            eventsUrl = input.eventsUrl,
            avatarUrl = input.avatarUrl,
            blog = input.blog,
            name = input.name,
            login = input.username,
        )
    }
}

object ListUserToListDBSearchUser : ListMapperImpl<User, DBSearchUser>(UserToDBSearchUser)

fun User.toDBSearchUser() = UserToDBSearchUser.map(this)

fun List<User>.toDBSearchUser(): List<DBSearchUser> = ListUserToListDBSearchUser.map(this)

object UserToDBFavoriteUser : Mapper<User, DBFavoriteUser> {
    override fun map(input: User): DBFavoriteUser {
        return DBFavoriteUser(
            id = input.id,
            publicRepos = input.publicRepos,
            publicGists = input.publicGists,
            email = input.email,
            location = input.location,
            hireable = input.hireable,
            bio = input.bio,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            company = input.company,
            twitterUsername = input.twitterUsername,
            following = input.following.count,
            followers = input.followers.count,
            gravatarId = input.gravatarId,
            subscriptionsUrl = input.subscriptionsUrl,
            receivedEventsUrl = input.receivedEventsUrl,
            reposUrl = input.reposUrl,
            starredUrl = input.starredUrl,
            siteAdmin = input.siteAdmin,
            url = input.url,
            organizationsUrl = input.organizationsUrl,
            type = input.type,
            nodeId = input.nodeId,
            htmlUrl = input.htmlUrl,
            gistsUrl = input.gistsUrl,
            followersUrl = input.followers.url,
            followingUrl = input.following.url,
            eventsUrl = input.eventsUrl,
            avatarUrl = input.avatarUrl,
            blog = input.blog,
            name = input.name,
            login = input.username,
        )
    }
}

object ListUserToListDBFavoriteUser : ListMapperImpl<User, DBFavoriteUser>(UserToDBFavoriteUser)

fun User.toDBFavoriteUser() = UserToDBFavoriteUser.map(this)

fun List<User>.toDBFavoriteUser(): List<DBFavoriteUser> = ListUserToListDBFavoriteUser.map(this)

object NetworkUserToDBUserFollowing : Mapper<SourceUser, DBUserFollowing> {
    override fun map(input: SourceUser): DBUserFollowing {
        return DBUserFollowing(
            id = input.id,
            login = input.login,
            name = input.name,
            blog = input.blog,
            avatarUrl = input.avatarUrl,
            eventsUrl = input.eventsUrl,
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
            updatedAt = DateFormat.toDate(input.updatedAt),
            createdAt = DateFormat.toDate(input.createdAt),
            bio = input.bio,
            hireable = input.hireable,
            location = input.location,
            email = input.email,
            publicGists = input.publicGists,
            publicRepos = input.publicRepos,
            followersUrl = input.followersUrl,
            followingUrl = input.followingUrl,
        )
    }
}

object ListNetworkUserToListUserFollowing : ListMapperImpl<SourceUser, User>(NetworkUserToUser)

fun SourceUser.toDBUserFollowing() = NetworkUserToDBUserFollowing.map(this)
fun SourceUser.toDBUserFollowing(userDbId: Int) = NetworkUserToDBUserFollowing.map(this).copy(userDbId = userDbId)


fun List<SourceUser>.toDBUserFollowing(): List<User> = ListNetworkUserToListUserFollowing.map(this)

object NetworkUserToDBUserFollowers : Mapper<SourceUser, DBUserFollowers> {
    override fun map(input: SourceUser): DBUserFollowers {
        return DBUserFollowers(
            id = input.id,
            login = input.login,
            name = input.name,
            blog = input.blog,
            avatarUrl = input.avatarUrl,
            eventsUrl = input.eventsUrl,
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
            updatedAt = DateFormat.toDate(input.updatedAt),
            createdAt = DateFormat.toDate(input.createdAt),
            bio = input.bio,
            hireable = input.hireable,
            location = input.location,
            email = input.email,
            publicGists = input.publicGists,
            publicRepos = input.publicRepos,
            followersUrl = input.followersUrl,
            followingUrl = input.followingUrl,
        )
    }
}

object ListNetworkUserToListUserFollowers : ListMapperImpl<SourceUser, User>(NetworkUserToUser)

fun SourceUser.toDBUserFollowers() = NetworkUserToDBUserFollowers.map(this)
fun SourceUser.toDBUserFollowers(userDbId: Int) = NetworkUserToDBUserFollowers.map(this).copy(userDbId = userDbId)

fun List<SourceUser>.toDBUserFollowers(): List<User> = ListNetworkUserToListUserFollowers.map(this)

object NetworkUserToDBPopularUser : Mapper<SourceUser, DBPopularUser> {
    override fun map(input: SourceUser): DBPopularUser {
        return DBPopularUser(
            id = input.id,
            login = input.login,
            name = input.name,
            blog = input.blog,
            avatarUrl = input.avatarUrl,
            eventsUrl = input.eventsUrl,
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
            updatedAt = DateFormat.toDate(input.updatedAt),
            createdAt = DateFormat.toDate(input.createdAt),
            bio = input.bio,
            hireable = input.hireable,
            location = input.location,
            email = input.email,
            publicGists = input.publicGists,
            publicRepos = input.publicRepos,
            followersUrl = input.followersUrl,
            followingUrl = input.followingUrl,
        )
    }
}

object ListNetworkUserToListPopularUser : ListMapperImpl<SourceUser, User>(NetworkUserToUser)

fun SourceUser.toDBPopularUser() = NetworkUserToDBPopularUser.map(this)

fun List<SourceUser>.toDBPopularUser(): List<User> = ListNetworkUserToListPopularUser.map(this)

object NetworkUserToDBDetailUser : Mapper<SourceUser, DBDetailUser> {
    override fun map(input: SourceUser): DBDetailUser {
        return DBDetailUser(
            id = input.id,
            login = input.login,
            name = input.name,
            blog = input.blog,
            avatarUrl = input.avatarUrl,
            eventsUrl = input.eventsUrl,
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
            updatedAt = DateFormat.toDate(input.updatedAt),
            createdAt = DateFormat.toDate(input.createdAt),
            bio = input.bio,
            hireable = input.hireable,
            location = input.location,
            email = input.email,
            publicGists = input.publicGists,
            publicRepos = input.publicRepos,
            followersUrl = input.followersUrl,
            followingUrl = input.followingUrl,
        )
    }
}

object ListNetworkUserToListDetailUser : ListMapperImpl<SourceUser, User>(NetworkUserToUser)

fun SourceUser.toDBDetailUser() = NetworkUserToDBDetailUser.map(this)

fun List<SourceUser>.toDBDetailUser(): List<User> = ListNetworkUserToListDetailUser.map(this)

fun DBSearchUser.toUser(favorite: User.Favorite): User = User(
    id = this.id,
    publicRepos = this.publicRepos,
    publicGists = this.publicGists,
    email = this.email,
    location = this.location,
    hireable = this.hireable,
    bio = this.bio,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
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
    username = this.login,
    following = User.Following(this.following, this.followingUrl),
    followers = User.Followers(this.followers, this.followersUrl),
    favorite = favorite
)

fun DBPopularUser.toUser(favorite: User.Favorite): User = User(
    id = this.id,
    publicRepos = this.publicRepos,
    publicGists = this.publicGists,
    email = this.email,
    location = this.location,
    hireable = this.hireable,
    bio = this.bio,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
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
    username = this.login,
    following = User.Following(this.following, this.followingUrl),
    followers = User.Followers(this.followers, this.followersUrl),
    favorite = favorite
)

fun DBUserFollowing.toUser(favorite: User.Favorite): User = User(
    id = this.id,
    publicRepos = this.publicRepos,
    publicGists = this.publicGists,
    email = this.email,
    location = this.location,
    hireable = this.hireable,
    bio = this.bio,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
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
    username = this.login,
    following = User.Following(this.following, this.followingUrl),
    followers = User.Followers(this.followers, this.followersUrl),
    favorite = favorite
)

fun DBUserFollowers.toUser(favorite: User.Favorite): User = User(
    id = this.id,
    publicRepos = this.publicRepos,
    publicGists = this.publicGists,
    email = this.email,
    location = this.location,
    hireable = this.hireable,
    bio = this.bio,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
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
    username = this.login,
    following = User.Following(this.following, this.followingUrl),
    followers = User.Followers(this.followers, this.followersUrl),
    favorite = favorite
)

fun DBDetailUser.toUser(favorite: User.Favorite): User = User(
    id = this.id,
    publicRepos = this.publicRepos,
    publicGists = this.publicGists,
    email = this.email,
    location = this.location,
    hireable = this.hireable,
    bio = this.bio,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
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
    username = this.login,
    following = User.Following(this.following, this.followingUrl),
    followers = User.Followers(this.followers, this.followersUrl),
    favorite = favorite
)

fun DBFavoriteUser.toUser(): User = User(
    id = this.id,
    publicRepos = this.publicRepos,
    publicGists = this.publicGists,
    email = this.email,
    location = this.location,
    hireable = this.hireable,
    bio = this.bio,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
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
    username = this.login,
    following = User.Following(this.following, this.followingUrl),
    followers = User.Followers(this.followers, this.followersUrl),
    favorite = User.Favorite(true, this.addedAt)
)