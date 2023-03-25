package com.novandi.githubuser.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getListUser(@Query("q") username: String) : Call<GithubResponse>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String) : Call<GithubResponse>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String) : Call<List<UserItems>>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String) : Call<List<UserItems>>
}