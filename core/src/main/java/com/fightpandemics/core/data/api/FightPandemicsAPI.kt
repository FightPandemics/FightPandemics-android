package com.fightpandemics.core.data.api

import com.fightpandemics.core.data.model.login.*
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface FightPandemicsAPI {

    @GET("api/posts")
    suspend fun getPosts(): Posts

    @GET("api/posts/")
    suspend fun getPosts(@Query("objective") objective: String?): List<Post>

    @Headers("content-type: application/json")
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<*>

    @POST("api/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>




    @POST("/api/auth/change-password")
    suspend fun changePassword(@Body email: String): Response<ChangePasswordResponse>

    companion object {
        // Staging API for Development
        const val API_ENDPOINT = "https://staging.fightpandemics.work/"
        // TODO - use the production url for release builds
        const val RELEASE_API_ENDPOINT = "https://fightpandemics.com/"
    }
}
