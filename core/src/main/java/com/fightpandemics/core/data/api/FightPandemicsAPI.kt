package com.fightpandemics.core.data.api

import com.fightpandemics.core.data.model.login.ChangePasswordResponse
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.SignUpRequest
import com.fightpandemics.core.data.model.login.SignUpResponse
import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import retrofit2.Response
import retrofit2.http.*

interface FightPandemicsAPI {

    @GET("api/posts")
    suspend fun getPosts(): Posts

    // TODO - Use Either<,> to get error
    // Get Post
    @GET("api/posts")
    suspend fun getPosts(
        @Query("objective") objective: String?,
        @Query("limit") limit: Int
    ): Response<List<Post>>

    // Login
    @Headers("No-Authentication: true") // no need to add authentication
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<*>

    @POST("api/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @POST("/api/auth/change-password")
    suspend fun changePassword(@Body email: String): Response<ChangePasswordResponse>


    // Edit a Post
    @PATCH("api/posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: String,
        @Body postRequest: PostRequest
    ): Response<Void>

    // Delete a Post
    @DELETE("api/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: String): Response<Void>

    // Like a Post
    @PUT("api/posts/{postId}/likes/{userId}")
    suspend fun likePost(
        @Path("postId") postId: String,
        @Path("userId") userId: String
    ): Response<Void>

    // Unlike a Post
    @DELETE("api/posts/{postId}/likes/{userId}")
    suspend fun unlikePost(
        @Path("postId") postId: String,
        @Path("userId") userId: String
    ): Response<Void>


    companion object {
        // Staging API for Development
        const val API_ENDPOINT = "https://staging.fightpandemics.work/"

        // TODO - use the production url for release builds
        const val RELEASE_API_ENDPOINT = "https://fightpandemics.com/"
    }
}
