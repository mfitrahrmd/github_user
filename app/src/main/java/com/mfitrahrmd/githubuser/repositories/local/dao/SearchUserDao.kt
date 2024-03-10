package com.mfitrahrmd.githubuser.repositories.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBSearchUser
import com.mfitrahrmd.githubuser.repositories.local.BatchUpdate

@Dao
abstract class SearchUserDao : BatchUpdate<DBSearchUser> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(user: DBSearchUser)

    @Delete
    abstract fun deleteOne(user: DBSearchUser)

    @Query("SELECT * FROM search_user ORDER BY dbId")
    abstract fun findAll(): PagingSource<Int, DBSearchUser>

    @Query("DELETE FROM search_user")
    abstract override fun deleteAll()
}