package com.novandi.githubuser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.novandi.githubuser.ui.main.SectionsPagerAdapter
import com.novandi.githubuser.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private val userViewModel by viewModels<UserViewModel>()

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarDetail.setNavigationOnClickListener { finish() }

        val username = intent.getStringExtra("username")
        showUserData(username.toString())
        userViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(window.decorView.rootView, snackbarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        userViewModel.isLoading.observe(this) { showLoading(it) }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        if (username != null) sectionsPagerAdapter.username = username
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    @SuppressLint("SetTextI18n")
    private fun showUserData(username: String) {
        userViewModel.getUser(username)
        userViewModel.user.observe(this) { user ->
            Glide.with(this).load(user.avatarUrl).into(binding.photoDetail)
            binding.nameDetail.text = user.name
            binding.usernameDetail.text = user.username
            if (user.email != null) {
                binding.emailDetail.text = user.email
            } else {
                binding.emailDetail.visibility = View.GONE
            }
            binding.tvFollowingDetail.text = "${user.followingTotal} Following"
            binding.tvFollowersDetail.text = "${user.followersTotal} Followers"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvFollowingDetail.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.tvFollowersDetail.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
}