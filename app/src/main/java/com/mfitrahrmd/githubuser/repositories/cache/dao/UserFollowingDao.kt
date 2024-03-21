package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowing
import com.mfitrahrmd.githubuser.repositories.cache.BatchOperation

@Dao
abstract class UserFollowingDao : BatchOperation<DBUserFollowing> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(user: DBUserFollowing)

    @Delete
    abstract suspend fun deleteOne(user: DBUserFollowing)

    @Query("SELECT * FROM user_following WHERE userDbId = :userDbId ORDER BY dbId")
    abstract fun findManyByUserDbId(userDbId: Int): PagingSource<Int, DBUserFollowing>

    @Query("DELETE FROM user_following WHERE userDbId = :userDbId")
    abstract suspend fun deleteManyByUserDbId(userDbId: Int)
}