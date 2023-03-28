package com.novandi.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.novandi.githubuser.database.UserFavorite
import com.novandi.githubuser.database.UserFavoriteDao
import com.novandi.githubuser.database.UserFavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserFavoriteRepository(application: Application) {
    private val mUserFavoriteDao: UserFavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserFavoriteRoomDatabase.getDatabase(application)
        mUserFavoriteDao = db.userFavoriteDao()
    }

    fun getAllUsers(): LiveData<List<UserFavorite>> = mUserFavoriteDao.getAllUsers()

    fun insertUser(user: UserFavorite) {
        executorService.execute { mUserFavoriteDao.insertUser(user) }
    }

    fun deleteUser(user: UserFavorite) {
        executorService.execute { mUserFavoriteDao.deleteUser(user) }
    }

    fun isUserFavorite(username: String): LiveData<Boolean> = mUserFavoriteDao.isUserFavorite(username)
}