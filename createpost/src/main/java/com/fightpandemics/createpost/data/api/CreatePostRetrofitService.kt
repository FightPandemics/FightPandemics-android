package com.fightpandemics.createpost.data.api

import com.squareup.moshi.Moshi
import dagger.Lazy
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CreatePostRetrofitService {

    companion object {
        private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(NetworkApi.RELEASE_API_ENDPOINT)

        @JvmStatic
        fun <S> createService(
            moshi: Moshi,
            okHttpClient: Lazy<OkHttpClient>,
            serviceClass: Class<S>,
        ): S {
            val retrofit = builder
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .callFactory(okHttpClient.get())
                .build()
            return retrofit.create(serviceClass)
        }
    }
}