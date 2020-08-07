package com.fightpandemics.data.remote

import retrofit2.Retrofit

interface ApiService {

    companion object Factory {
        fun create(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
    }
}