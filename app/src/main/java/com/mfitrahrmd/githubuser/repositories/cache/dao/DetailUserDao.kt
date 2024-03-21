package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBDetailUser

@Dao
interface DetailUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(user: DBDetailUser)

    @Delete
    suspend fun deleteOne(user: DBDetailUser)

    @Query("DELETE FROM detail_user WHERE login = :username")
    suspend fun deleteOneByUsername(username: String)

    @Query("SELECT * FROM detail_user WHERE login = :username")
    suspend fun findOneByUsername(username: String): DBDetailUser?

    @Query("DELETE FROM detail_user")
    suspend fun deleteAll()
}