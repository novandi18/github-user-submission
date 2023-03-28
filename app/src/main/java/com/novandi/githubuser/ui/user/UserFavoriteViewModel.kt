package com.novandi.githubuser.ui.user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novandi.githubuser.Event
import com.novandi.githubuser.database.UserFavorite
import com.novandi.githubuser.repository.UserFavoriteRepository

class UserFavoriteViewModel(application: Application) : ViewModel() {
    private val mUserFavoriteRepository: UserFavoriteRepository = UserFavoriteRepository(application)

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun insertUser(user: UserFavorite) {
        mUserFavoriteRepository.insertUser(user)
        _snackbarText.value = Event("Successfully added to favourites")
    }

    fun deleteUser(user: UserFavorite) {
        mUserFavoriteRepository.deleteUser(user)
        _snackbarText.value = Event("Successfully deleted from favourites")
    }

    fun isUserFavorite(username: String): LiveData<Boolean> = mUserFavoriteRepository.isUserFavorite(username)
}