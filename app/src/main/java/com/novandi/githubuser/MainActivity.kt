package com.novandi.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.novandi.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        val layoutManager = GridLayoutManager(this, 2)
        binding.rvGithub.layoutManager = layoutManager

        showResult()
        mainViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snakebarText ->
                Snackbar.make(window.decorView.rootView, snakebarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        mainViewModel.isLoading.observe(this) { showLoading(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.cari_username)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query
                mainViewModel.isLoading.observe(this@MainActivity) { showLoading(it) }
                mainViewModel.searchUsers(query!!)
                showResult()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        menu.findItem(R.id.search).setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                if (searchQuery != null) {
                    mainViewModel.searchUsers()
                    mainViewModel.isLoading.observe(this@MainActivity) { showLoading(it) }
                    showResult()
                    searchQuery = null
                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun showResult() {
        mainViewModel.users.observe(this) { users ->
            val listUserAdapter = ListUserAdapter(users)
            binding.rvGithub.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserItems) {
                    val intent = Intent(this@MainActivity, UserActivity::class.java)
                    intent.putExtra(TAG_USERNAME, data.usernameItem)
                    startActivity(intent)
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvGithub.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    companion object {
        const val TAG_USERNAME = "username"
    }
}