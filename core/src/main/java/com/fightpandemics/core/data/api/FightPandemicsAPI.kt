package com.fightpandemics.core.data.api

import com.fightpandemics.core.data.model.login.*
import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import retrofit2.Response
import retrofit2.http.*

interface FightPandemicsAPI {

    @GET("api/posts")
    suspend fun getPosts(): Posts

    @GET("api/posts")
    suspend fun getPosts(
        @Query("objective") objective: String?,
        @Query("limit") limit: Int
    ): List<Post>

    @Headers("No-Authentication: true") // no need to add authentication
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<*>

    @POST("api/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers("Login: true")
    @POST("api/users")
    suspend fun completeProfile(@Body completeProfileRequest: CompleteProfileRequest): Response<CompleteProfileResponse>

    @POST("/api/auth/change-password")
    suspend fun changePassword(@Body email: String): Response<ChangePasswordResponse>


    @PATCH("api/posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: String,
        @Body postRequest: PostRequest
    ): Response<Void>

    @DELETE("api/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: String): Response<Void>


    @PUT("api/posts/{postId}/likes/{userId}")
    suspend fun likePost(
        @Path("postId") postId: String,
        @Path("userId") userId: String
    ): Response<Void>

    @DELETE("api/posts/{postId}/likes/{userId}")
    suspend fun unlikePost(
        @Path("postId") postId: String,
        @Path("userId") userId: String
    ): Response<Void>

    // Profile
    @GET("api/users/current")
    suspend fun getCurrentUser(): IndividualProfileResponse



    companion object {
        // Staging API for Development
        const val API_ENDPOINT = "https://staging.fightpandemics.work/"

        // TODO - use the production url for release builds
        const val RELEASE_API_ENDPOINT = "https://fightpandemics.com/"
    }
}
