package com.novandi.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.novandi.githubuser.database.UserFavorite

class UserFavoriteDiffCallback(private val mOldUserFavoriteList: List<UserFavorite>, private val mNewUserFavoriteList: List<UserFavorite>) : DiffUtil.Callback() {
    override fun getOldListSize() = mOldUserFavoriteList.size

    override fun getNewListSize() = mNewUserFavoriteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldUserFavoriteList[oldItemPosition].id == mNewUserFavoriteList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldUserFavoriteList[oldItemPosition].username == mNewUserFavoriteList[newItemPosition].username
}