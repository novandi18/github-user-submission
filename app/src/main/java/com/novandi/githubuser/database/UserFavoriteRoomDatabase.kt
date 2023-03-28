package com.novandi.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFavorite::class], version = 1, exportSchema = false)
abstract class UserFavoriteRoomDatabase : RoomDatabase() {
    abstract fun userFavoriteDao(): UserFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: UserFavoriteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserFavoriteRoomDatabase {
            if (INSTANCE == null) synchronized(UserFavoriteRoomDatabase::class.java) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, UserFavoriteRoomDatabase::class.java,
                    "user_fav_db").build()
            }

            return INSTANCE as UserFavoriteRoomDatabase
        }
    }
}