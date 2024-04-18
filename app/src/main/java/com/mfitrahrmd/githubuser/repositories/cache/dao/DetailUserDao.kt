package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mfitrahrmd.githubuser.entities.db.DBDetailUser
import com.mfitrahrmd.githubuser.entities.db.DBDetailUserWithFavorite
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DetailUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertOne(user: DBDetailUser)

    @Delete
    abstract suspend fun deleteOne(user: DBDetailUser)

    @Query("DELETE FROM detail_user WHERE login = :username")
    abstract suspend fun deleteOneByUsername(username: String)

    @Query("SELECT * FROM detail_user WHERE login = :username")
    abstract suspend fun findOneByUsername(username: String): DBDetailUser?

    @Transaction
    @Query("SELECT * FROM detail_user WHERE login = :username")
    abstract fun findOneByUsernameWithFavorite(username: String): Flow<DBDetailUserWithFavorite>

    @Query("DELETE FROM detail_user")
    abstract suspend fun deleteAll()
}