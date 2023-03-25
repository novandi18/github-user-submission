package com.novandi.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novandi.githubuser.Event
import com.novandi.githubuser.api.GithubResponse
import com.novandi.githubuser.api.UserItems
import com.novandi.githubuser.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<UserItems>>()
    val users: LiveData<List<UserItems>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isVisible = MutableLiveData<Boolean>()

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        searchUsers()
    }

    fun searchUsers(username: String = "arif") {
        _isLoading.value = true
        _isVisible.value = false
        val client = ApiConfig.getApiService().getListUser(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                _isVisible.value = true
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    _snackbarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _isVisible.value = true
                _snackbarText.value = Event(t.message.toString())
            }
        })
    }
}