package com.novandi.githubuser.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.novandi.githubuser.database.UserFavorite
import com.novandi.githubuser.databinding.ActivityFavoriteBinding
import com.novandi.githubuser.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Favorite Users"
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favoriteViewModel = obtainFavoriteViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllUsers().observe(this) { userFavList ->
            if (userFavList != null) {
                binding?.ivEmpty?.visibility = if (userFavList.isNotEmpty()) View.GONE else View.VISIBLE
                adapter.setListFavorite(userFavList)
            }
        }

        adapter = FavoriteAdapter()

        binding?.rvUserFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvUserFavorite?.setHasFixedSize(true)
        binding?.rvUserFavorite?.adapter = adapter

        adapter.setOnItemClickDeleteCallback(object : FavoriteAdapter.OnItemClickDeleteCallback {
            override fun onItemClicked(data: UserFavorite) {
                favoriteViewModel.deleteUser(data)
            }
        })

        favoriteViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(window.decorView.rootView, snackbarText, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}