package com.fightpandemics.core.networking.network

import com.fightpandemics.core.Variables
import com.fightpandemics.core.networking.APIService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkHelper {

    //Todo - CertPinning, Logging, Interceptors etc. can be added

    fun newAPIService(): APIService {
        val b = OkHttpClient.Builder()
        b.readTimeout(60, TimeUnit.SECONDS)
        b.writeTimeout(60, TimeUnit.SECONDS)
        val client = b.build()
        val builder: Retrofit = Retrofit.Builder()
            .baseUrl(Variables.API_ENDPOINT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        return builder.create(APIService::class.java)
    }
}
