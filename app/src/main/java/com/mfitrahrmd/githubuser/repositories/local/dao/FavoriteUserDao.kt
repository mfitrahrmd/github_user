package com.mfitrahrmd.githubuser.repositories.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.githubuser.entities.db.DBFavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(user: DBFavoriteUser)

    @Delete
    fun deleteOne(user: DBFavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun findAll(): LiveData<List<DBFavoriteUser>>

    @Query("SELECT * FROM favorite_user WHERE id = :id")
    fun findOneById(id: Int): DBFavoriteUser?
}