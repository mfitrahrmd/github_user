package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.entities.db.DBPopularUserWithFavorite
import com.mfitrahrmd.githubuser.repositories.cache.BatchOperation

@Dao
abstract class PopularUserDao : BatchOperation<DBPopularUser> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(user: DBPopularUser)

    @Delete
    abstract suspend fun deleteOne(user: DBPopularUser)

    @Query("SELECT * FROM popular_user ORDER BY dbId")
    abstract fun findAll(): PagingSource<Int, DBPopularUser>

    @Query("DELETE FROM popular_user")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM popular_user LEFT JOIN favorite_user ON popular_user.login = favorite_user.login")
    abstract fun findAllWithFavorite(): PagingSource<Int, DBPopularUserWithFavorite>
}