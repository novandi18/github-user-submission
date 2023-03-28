package com.novandi.githubuser.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GithubResponse(
	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<UserItems>,
)

@Parcelize
data class UserItems(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val username: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("followers")
	val followersTotal: Int,

	@field:SerializedName("following")
	val followingTotal: Int,

	@field:SerializedName("email")
	val email: String?,

	@field:SerializedName("name")
	val name: String,
) : Parcelable