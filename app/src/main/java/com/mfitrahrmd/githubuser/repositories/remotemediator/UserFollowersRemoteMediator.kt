package com.mfitrahrmd.githubuser.repositories.remotemediator

import com.mfitrahrmd.githubuser.base.BaseRemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowers
import com.mfitrahrmd.githubuser.entities.network.SourceUser
import com.mfitrahrmd.githubuser.mapper.toDBUserFollowers
import com.mfitrahrmd.githubuser.mapper.toDBUserFollowing
import com.mfitrahrmd.githubuser.repositories.cache.dao.DetailUserDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.UserFollowersDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import com.mfitrahrmd.githubuser.repositories.datasource.remote.services.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserFollowersRemoteMediator(
    private val _username: String,
    private val _dataSource: DataSource,
    private val _userFollowersDao: UserFollowersDao,
    private val _detailUserDao: DetailUserDao
) : BaseRemoteMediator<DBUserFollowers, SourceUser>(
    _userFollowersDao
) {
    override suspend fun fetch(page: Int, pageSize: Int): List<SourceUser> {
        val followers = _dataSource.listUserFollowers(_username, page, pageSize)

        return followers
    }

    override suspend fun cleanLocalData() {
        val user = _detailUserDao.findOneByUsername(_username)
        if (user != null) {
            withContext(Dispatchers.IO) {
                _userFollowersDao.deleteManyByUserDbId(user.dbId)
            }
        }
    }

    override suspend fun toLocalEntity(networkEntity: SourceUser): DBUserFollowers {
        val user = _detailUserDao.findOneByUsername(_username)
        if (user == null) {
            throw Exception("user not found")
        }

        return networkEntity.toDBUserFollowers(user.dbId)
    }

    override suspend fun upsertLocalData(localEntities: List<DBUserFollowers>) {
        _userFollowersDao.insertMany(localEntities)
    }
}