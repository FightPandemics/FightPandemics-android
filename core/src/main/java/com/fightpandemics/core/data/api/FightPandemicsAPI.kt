package com.fightpandemics.core.data.api

import com.fightpandemics.core.data.model.login.*
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FightPandemicsAPI {

    @GET("api/posts")
    suspend fun getPosts(): Posts

    @GET("api/posts/")
    suspend fun getPosts(@Query("objective") objective: String?): List<Post>

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>




    @POST("/api/auth/signup")
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
