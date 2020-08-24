package com.fightpandemics.data.remote

import com.fightpandemics.data.model.login.LoginRequest
import com.fightpandemics.data.model.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestInterface {

//    @GET("/api")
//    suspend fun getJobs(): Response<List<Job>>

    @POST("/api/auth/login")
    suspend fun login(@Body loginReq: LoginRequest): Response<LoginResponse>

    companion object {
        const val ENDPOINT = "https://remoteok.io"

        // Todo : Please replace with staging during development
        const val API_ENDPOINT = "https://fightpandemics.com"
    }
}
