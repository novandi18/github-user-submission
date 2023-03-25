package com.novandi.githubuser.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: SettingsPreferences) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch { pref.saveThemeSetting(isDarkMode) }
    }
}