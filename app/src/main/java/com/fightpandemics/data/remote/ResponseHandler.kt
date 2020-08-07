package com.fightpandemics.data.remote

import com.fightpandemics.data.Resource
import okhttp3.internal.http2.ErrorCode
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException

open class ResponseHandler {

    fun <T: Any> handleSuccess(data: T): Resource<T> {
        return Resource.Success(data)
    }

    fun <T: Any> handleException(e: Exception): Resource<T> {
        return when(e) {
            is HttpException -> Resource.Error(getErrorMessage(e.code()), e)
            is SocketTimeoutException -> Resource.Error(getErrorMessage(ErrorCode.SETTINGS_TIMEOUT.httpCode), e)
            else -> Resource.Error(getErrorMessage(Int.MAX_VALUE), e)
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when(code) {
            ErrorCode.SETTINGS_TIMEOUT.httpCode -> "Timeout"
            400 -> "Bad Request"
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }
}