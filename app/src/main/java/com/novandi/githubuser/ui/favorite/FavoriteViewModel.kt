package com.novandi.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novandi.githubuser.Event
import com.novandi.githubuser.database.UserFavorite
import com.novandi.githubuser.repository.UserFavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserFavoriteRepository: UserFavoriteRepository = UserFavoriteRepository(application)

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getAllUsers(): LiveData<List<UserFavorite>> = mUserFavoriteRepository.getAllUsers()

    fun deleteUser(user: UserFavorite) {
        mUserFavoriteRepository.deleteUser(user)
        _snackbarText.value = Event("Successfully deleted from favourites")
    }
}