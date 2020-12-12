package com.fightpandemics.createpost.data.api

import com.fightpandemics.core.data.model.posts.Post
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkApi {

    @POST("api/posts")
    suspend fun createPost(@Body post: Post): Any

    companion object {
        const val STAGING_API_ENDPOINT = "https://staging.fightpandemics.work/"
        const val RELEASE_API_ENDPOINT = "https://fightpandemics.com/"
    }
}