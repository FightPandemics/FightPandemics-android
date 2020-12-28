package com.fightpandemics.createpost.data.api

import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.data.model.CreatePostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkApi {

    @POST("api/posts")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest): Response<*>

    companion object {
        const val STAGING_API_ENDPOINT = "https://staging.fightpandemics.work/"
        const val RELEASE_API_ENDPOINT = "https://fightpandemics.com/"
    }
}