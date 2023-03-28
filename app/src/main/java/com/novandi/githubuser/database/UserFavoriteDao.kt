package com.novandi.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: UserFavorite)

    @Delete
    fun deleteUser(user: UserFavorite)

    @Query("SELECT * FROM UserFavorite ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<UserFavorite>>

    @Query("SELECT EXISTS(SELECT * FROM UserFavorite WHERE username = :username)")
    fun isUserFavorite(username: String): LiveData<Boolean>
}