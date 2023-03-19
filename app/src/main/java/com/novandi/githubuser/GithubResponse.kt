package com.novandi.githubuser

import com.google.gson.annotations.SerializedName

data class GithubResponse(
	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<UserItems>,

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
	val name: String
)

data class UserItems(
	@field:SerializedName("login")
	val usernameItem: String,

	@field:SerializedName("avatar_url")
	val avatarUrlItem: String,
)
