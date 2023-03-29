package com.novandi.githubuser.api

import org.junit.Assert.assertEquals
import org.junit.Test

class ApiConfigTest {
    private val dummySearch = "novandi"
    private val dummyUser = "novandi18"
    private val dummyResponse = 200

    @Test
    fun testSearchUserResponse() {
        val api: ApiService = ApiConfig.getApiService()
        val response = api.getListUser(dummySearch).execute()
        assertEquals(dummyResponse, response.code())
    }

    @Test
    fun testUserDetailResponse() {
        val api: ApiService = ApiConfig.getApiService()
        val response = api.getUser(dummyUser).execute()
        assertEquals(dummyResponse, response.code())
    }
}