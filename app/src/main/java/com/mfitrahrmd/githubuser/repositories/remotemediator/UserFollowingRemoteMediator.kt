package com.mfitrahrmd.githubuser.repositories.remotemediator

import com.mfitrahrmd.githubuser.base.BaseRemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowing
import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.mapper.toDBUserFollowing
import com.mfitrahrmd.githubuser.repositories.cache.dao.DetailUserDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.UserFollowingDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserFollowingRemoteMediator(
    private val _username: String,
    private val _dataSource: DataSource,
    private val _userFollowingDao: UserFollowingDao,
    private val _detailUserDao: DetailUserDao
) : BaseRemoteMediator<DBUserFollowing, RemoteUser>(_userFollowingDao) {
    override suspend fun fetch(page: Int, pageSize: Int): List<RemoteUser> {
        val following = _dataSource.listUserFollowing(_username, page, pageSize)

        return following
    }

    override suspend fun toLocalEntity(networkEntity: RemoteUser): DBUserFollowing {
        val user = _detailUserDao.findOneByUsername(_username)
        if (user == null) {
            throw Exception("user not found")
        }

        return networkEntity.toDBUserFollowing(user.dbId)
    }

    override suspend fun cleanLocalData() {
        val user = _detailUserDao.findOneByUsername(_username)
        if (user != null) {
            withContext(Dispatchers.IO) {
                _userFollowingDao.deleteManyByUserDbId(user.dbId)
            }
        }
    }

    override suspend fun upsertLocalData(localEntities: List<DBUserFollowing>) {
        _userFollowingDao.insertMany(localEntities)
    }
}