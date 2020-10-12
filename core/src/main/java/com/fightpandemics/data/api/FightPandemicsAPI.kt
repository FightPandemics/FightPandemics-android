package com.fightpandemics.data.api

import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.data.model.posts.Posts
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FightPandemicsAPI {

    @GET("api/posts")
    suspend fun getPosts(): Posts

    @GET("api/posts/")
    suspend fun getPosts(@Query("objective") objective: String?): List<Post>

    companion object {
        // Staging API for Development
        const val API_ENDPOINT = "https://staging.fightpandemics.work/"
        // TODO - use the production url for release builds
        const val RELEASE_API_ENDPOINT = "https://fightpandemics.com/"
    }
}
