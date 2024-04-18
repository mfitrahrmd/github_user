package com.mfitrahrmd.githubuser.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toDBFavoriteUser
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.FavoriteUserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserFavoriteRepositoryImpl(
    private val _userFavoriteDao: FavoriteUserDao
) : UserFavoriteRepository {
    override suspend fun add(user: User) {
        _userFavoriteDao.insertOne(user.toDBFavoriteUser())
    }

    override suspend fun remove(user: User) {
        _userFavoriteDao.deleteAllByUsername(user.username)
    }

    override fun get(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
            pagingSourceFactory = { _userFavoriteDao.findAll() }
        ).flow.map {
            it.map { dbFU ->
                dbFU.toUser()
            }
        }
    }

    override fun getAll(): List<User> {
        return _userFavoriteDao.getAll().map {
            it.toUser()
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50
    }
}