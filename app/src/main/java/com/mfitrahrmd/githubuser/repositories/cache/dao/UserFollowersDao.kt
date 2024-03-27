package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowers
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowersWithFavorite
import com.mfitrahrmd.githubuser.repositories.cache.BatchOperation

@Dao
abstract class UserFollowersDao : BatchOperation<DBUserFollowers> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(user: DBUserFollowers)

    @Delete
    abstract suspend fun deleteOne(user: DBUserFollowers)

    @Query("SELECT * FROM user_followers LEFT JOIN favorite_user ON user_followers.login = favorite_user.login WHERE user_followers.userDbId = :userDbId ORDER BY user_followers.dbId")
    abstract fun findManyByUserDbIdWithFavorite(userDbId: Int): PagingSource<Int, DBUserFollowersWithFavorite>

    @Query("DELETE FROM user_followers WHERE userDbId = :userDbId")
    abstract suspend fun deleteManyByUserDbId(userDbId: Int)
}