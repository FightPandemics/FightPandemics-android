package com.fightpandemics.data.remote

import dagger.Lazy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    companion object {
        private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(RequestInterface.ENDPOINT)
            // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())

        @JvmStatic
        fun <S> createService(okHttpClient: Lazy<OkHttpClient>, serviceClass: Class<S>): S {
            val retrofit = builder
                .callFactory(okHttpClient.get()) // Deferred OkHttp Initialization
                .build()
            return retrofit.create(serviceClass)
        }
    }
}
