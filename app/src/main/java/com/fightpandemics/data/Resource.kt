package com.fightpandemics.data

sealed class Resource<out T> {

    class Success<out T>(val data: T) : Resource<T>()
    class Error<out T>(val message: String, val throwable: Throwable) : Resource<T>()
    class Loading<out T> : Resource<T>()
}