package com.fightpandemics.di.providers

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    fun provide(converterFactory: GsonConverterFactory, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .client(client)
        .baseUrl("")
        .build()

}