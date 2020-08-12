package com.fightpandemics.di

import com.fightpandemics.data.local.AppDatabase
import com.fightpandemics.data.remote.ApiService
import com.fightpandemics.di.providers.AppDatabaseProvider
import com.fightpandemics.di.providers.HttpLoggingInterceptorProvider
import com.fightpandemics.di.providers.OkHttpClientProvider
import com.fightpandemics.di.providers.RetrofitProvider
import com.fightpandemics.ui.home.all.AllViewModel
import com.fightpandemics.ui.home.offers.OffersViewModel
import com.fightpandemics.ui.home.requests.RequestsViewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val dbModule = module {
    single { AppDatabaseProvider.provide(get()) }
}

val daoModule = module {
    single { get<AppDatabase>().dao() }
}

val networkModule = module {
    single { HttpLoggingInterceptorProvider.provide() }
    single { OkHttpClientProvider.provide(get()) }
    single { GsonConverterFactory.create() }
    single { RetrofitProvider.provide(get(), get()) }
}

val apiServiceModule = module {
    single { ApiService.create(get()) }
}

val viewModelModule = module {
    factory { AllViewModel() }
    factory { OffersViewModel() }
    factory { RequestsViewModel() }
}

val repositoryModule = module{
}