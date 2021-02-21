package com.fightpandemics.core.data.api

import com.fightpandemics.core.data.model.login.ChangePasswordResponse
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.SignUpRequest
import com.fightpandemics.core.data.model.login.CompleteProfileResponse
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.SignUpResponse
import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import com.fightpandemics.core.data.model.userlocation.LocationResponse
import com.fightpandemics.core.data.model.userlocationdetails.LocationDetails
import com.fightpandemics.core.data.model.userlocationpredictions.LocationPrediction
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileResponse
import retrofit2.Response
import retrofit2.http.*

interface FightPandemicsAPI {

    @GET("api/posts")
    suspend fun getPosts(): Posts

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

    @Headers("Login: true")
    @POST("api/users")
    suspend fun completeProfile(@Body completeProfileRequest: CompleteProfileRequest): Response<CompleteProfileResponse>

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
    suspend fun deletePost(@Path("postId") postId: String): Response<*>

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

    // Get User Location
    @Headers("No-Authentication: true")
    @GET("api/geo/location-reverse-geocode")
    suspend fun getUserLocation(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
    ): Response<LocationResponse>

    // Get User Searching For Location.
    @GET("api/geo/address-predictions")
    suspend fun getLocationPredictions(
        @Query("input") input: String,
        @Query("sessiontoken") sessiontoken: String
    ): Response<LocationPrediction>

    // Get More Details Of A Place.
    @GET("api/geo/location-details")
    suspend fun getLocationDetails(
        @Query("placeId") placeId: String,
        @Query("sessiontoken") sessiontoken: String?
    ): Response<LocationDetails>

    // Profile API calls
    @GET("api/users/current")
    suspend fun getCurrentUser(): IndividualProfileResponse

    @PATCH("api/users/current")
    suspend fun updateCurrentUserProfile(
        @Body patchIndividualProfileRequest: PatchIndividualProfileRequest
    ): Response<PatchIndividualProfileResponse>

    @PATCH("api/users/current")
    suspend fun updateCurrentUserAccount(
        @Body patchIndividualAccountRequest: PatchIndividualAccountRequest
    ): Response<PatchIndividualProfileResponse>

    @GET("api/posts")
    suspend fun getPostsByAuthor(
        @Query("ignoreUserLocation") ignoreUserLocation: Boolean,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("authorId") authorId: String,
    ): List<Post>

    companion object {
        // Staging API for Development
        const val API_ENDPOINT = "https://fightpandemics.com/"

        // TODO - use the production url for release builds
        const val RELEASE_API_ENDPOINT = "https://fightpandemics.com/"
    }
}
