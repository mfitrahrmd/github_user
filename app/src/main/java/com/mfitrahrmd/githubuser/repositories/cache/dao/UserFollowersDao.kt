package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowers
import com.mfitrahrmd.githubuser.repositories.cache.BatchOperation

@Dao
abstract class UserFollowersDao : BatchOperation<DBUserFollowers> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(user: DBUserFollowers)

    @Delete
    abstract suspend fun deleteOne(user: DBUserFollowers)

    @Query("SELECT * FROM user_followers WHERE userDbId = :userDbId ORDER BY dbId")
    abstract fun findManyByUserDbId(userDbId: Int): PagingSource<Int, DBUserFollowers>

    @Query("DELETE FROM user_followers WHERE userDbId = :userDbId")
    abstract suspend fun deleteManyByUserDbId(userDbId: Int)
}