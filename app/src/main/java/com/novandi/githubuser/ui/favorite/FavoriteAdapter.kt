package com.novandi.githubuser.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.novandi.githubuser.database.UserFavorite
import com.novandi.githubuser.databinding.ItemUserFavoriteBinding
import com.novandi.githubuser.helper.UserFavoriteDiffCallback
import com.novandi.githubuser.ui.user.UserActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private lateinit var onItemClickDeleteCallback: OnItemClickDeleteCallback
    private val listFavorite = ArrayList<UserFavorite>()

    fun setListFavorite(listFavorite: List<UserFavorite>) {
        val diffCallback = UserFavoriteDiffCallback(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FavoriteViewHolder(private val binding: ItemUserFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userFavorite: UserFavorite) {
            with(binding) {
                Glide.with(this.cvUser).load(userFavorite.avatarUrl).into(this.ivUserFavorite)
                tvUserFavorite.text = userFavorite.username
                cvUser.setOnClickListener {
                    val intent = Intent(it.context, UserActivity::class.java)
                    intent.putExtra(UserActivity.EXTRA_USER_FAV, userFavorite)
                    it.context.startActivity(intent)
                }
                btnDeleteUserFavorite.setOnClickListener {
                    onItemClickDeleteCallback.onItemClicked(userFavorite)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemUserFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount() = listFavorite.size

    fun setOnItemClickDeleteCallback(onItemClickDeleteCallback: OnItemClickDeleteCallback) {
        this.onItemClickDeleteCallback = onItemClickDeleteCallback
    }

    interface OnItemClickDeleteCallback {
        fun onItemClicked(data: UserFavorite)
    }
}