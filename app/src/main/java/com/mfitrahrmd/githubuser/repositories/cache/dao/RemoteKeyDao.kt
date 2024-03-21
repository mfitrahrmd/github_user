package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM remote_key WHERE userId = :userId")
    suspend fun findByUserId(userId: Int): RemoteKey?

    @Query("DELETE FROM remote_key")
    suspend fun deleteAll()
}