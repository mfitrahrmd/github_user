package com.mfitrahrmd.githubuser.repositories.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mfitrahrmd.githubuser.entities.db.DBFavoriteUser
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.entities.db.DBSearchUser
import com.mfitrahrmd.githubuser.entities.db.RemoteKey
import com.mfitrahrmd.githubuser.repositories.local.dao.FavoriteUserDao
import com.mfitrahrmd.githubuser.repositories.local.dao.PopularUserDao
import com.mfitrahrmd.githubuser.repositories.local.dao.RemoteKeyDao
import com.mfitrahrmd.githubuser.repositories.local.dao.SearchUserDao

@TypeConverters(Converters::class)
@Database(
    entities = [DBFavoriteUser::class, DBPopularUser::class, DBSearchUser::class, RemoteKey::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao
    abstract fun popularUserDao(): PopularUserDao
    abstract fun searchUserDao(): SearchUserDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        @Volatile
        private var _INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getInstance(ctx: Context): UserDatabase {
            if (_INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    _INSTANCE = Room.databaseBuilder(
                        ctx.applicationContext,
                        UserDatabase::class.java,
                        "user_database"
                    ).build()
                }
            }

            return _INSTANCE!!
        }
    }
}