package com.mfitrahrmd.githubuser.repositories.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.repositories.local.BatchUpdate

@Dao
abstract class PopularUserDao : BatchUpdate<DBPopularUser> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(user: DBPopularUser)

    @Delete
    abstract fun deleteOne(user: DBPopularUser)

    @Query("SELECT * FROM popular_user ORDER BY dbId")
    abstract fun findAll(): PagingSource<Int, DBPopularUser>

    @Query("DELETE FROM popular_user")
    abstract override fun deleteAll()
}