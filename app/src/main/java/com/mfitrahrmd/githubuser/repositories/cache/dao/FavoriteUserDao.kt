package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBFavoriteUser
import com.mfitrahrmd.githubuser.repositories.cache.BatchOperation
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavoriteUserDao : BatchOperation<DBFavoriteUser> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertOne(user: DBFavoriteUser)

    @Delete
    abstract suspend fun deleteOne(user: DBFavoriteUser)

    @Query("DELETE FROM favorite_user WHERE login = :username")
    abstract suspend fun deleteAllByUsername(username: String)

    @Query("SELECT * FROM favorite_user ORDER BY dbId")
    abstract fun findAll(): PagingSource<Int, DBFavoriteUser>

    @Query("SELECT * FROM favorite_user WHERE dbId = :dbId")
    abstract fun findOneByDbId(dbId: Int): DBFavoriteUser?

    @Query("SELECT * FROM favorite_user WHERE login = :username")
    abstract fun findOneByUsername(username: String): DBFavoriteUser?

    @Query("SELECT * FROM favorite_user WHERE login = :username")
    abstract fun findOneByUsernameFlow(username: String): Flow<DBFavoriteUser?>

    @Query("DELETE FROM favorite_user")
    abstract suspend fun deleteAll()
}