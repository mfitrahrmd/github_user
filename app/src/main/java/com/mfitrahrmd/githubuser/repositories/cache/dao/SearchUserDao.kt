package com.mfitrahrmd.githubuser.repositories.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBSearchUser
import com.mfitrahrmd.githubuser.repositories.cache.BatchOperation

@Dao
abstract class SearchUserDao : BatchOperation<DBSearchUser> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(user: DBSearchUser)

    @Delete
    abstract suspend fun deleteOne(user: DBSearchUser)

    @Query("SELECT * FROM search_user ORDER BY dbId")
    abstract fun findAll(): PagingSource<Int, DBSearchUser>

    @Query("DELETE FROM search_user")
    abstract suspend fun deleteAll()
}