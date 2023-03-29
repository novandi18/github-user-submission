package com.novandi.githubuser.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.novandi.githubuser.R
import com.novandi.githubuser.database.UserFavorite
import com.novandi.githubuser.ui.main.SectionsPagerAdapter
import com.novandi.githubuser.databinding.ActivityUserBinding
import com.novandi.githubuser.helper.ViewModelFactory
import com.novandi.githubuser.ui.main.MainActivity

@Suppress("DEPRECATION")
class UserActivity : AppCompatActivity() {
    private var userFavorite: UserFavorite? = null
    private var username: String? = null

    private lateinit var binding: ActivityUserBinding
    private lateinit var userFavoriteViewModel: UserFavoriteViewModel
    private val userViewModel by viewModels<UserViewModel>()

    companion object {
        const val EXTRA_USER_FAV = "extra_user_fav"
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userFavoriteViewModel = obtainUserFavoriteViewModel(this@UserActivity)
        userFavorite = intent.getParcelableExtra(EXTRA_USER_FAV)
        if (userFavorite == null) userFavorite = UserFavorite()
        userFavorite?.let {
            username = if (it.username == null) intent.getStringExtra(MainActivity.EXTRA_USER_MAIN) else it.username.toString()
        }

        binding.userAppBar.setNavigationOnClickListener { finish() }
        binding.userAppBar.menu.getItem(0).icon?.setTint(resources.getColor(R.color.grey))

        showUserData(username.toString())
        userViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(window.decorView.rootView, snackbarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        userFavoriteViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(window.decorView.rootView, snackbarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        userViewModel.isLoading.observe(this) { showLoading(it) }
        userFavoriteViewModel.isUserFavorite(username.toString()).observe(this) { isFav ->
            if (isFav) {
                val btnFavoriteToggle = binding.userAppBar.menu.getItem(0)
                btnFavoriteToggle.isChecked = isFav
                btnFavoriteToggle.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                btnFavoriteToggle.icon?.setTint(resources.getColor(R.color.grey))
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        if (username != null) sectionsPagerAdapter.username = username as String
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

            setUserFavorite(user.id, username, user.avatarUrl)
        }
    }

    private fun setUserFavorite(id: Int, username: String, avatarUrl: String) {
        binding.userAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_user_favorite -> {
                    menuItem.isChecked = !menuItem.isChecked
                    userFavorite.let { userFav ->
                        userFav?.id = id
                        userFav?.username = username
                        userFav?.avatarUrl = avatarUrl
                    }

                    if (menuItem.isChecked) {
                        userFavoriteViewModel.insertUser(userFavorite as UserFavorite)
                        menuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                    } else {
                        userFavoriteViewModel.deleteUser(userFavorite as UserFavorite)
                        menuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_outlined)
                    }

                    false
                }
                R.id.btn_user_share -> {
                    val shareUser: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Kunjungi profil Github dari $username untuk mendapatkan source code terbaru!\n " +
                                "link profil : https://github.com/$username")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(shareUser, null)
                    startActivity(shareIntent)

                    false
                }
                else -> false
            }
        }
    }

    private fun obtainUserFavoriteViewModel(activity: AppCompatActivity): UserFavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserFavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvFollowingDetail.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.tvFollowersDetail.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
}