package com.fightpandemics.core.utils

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import retrofit2.Response

inline fun <reified T> Response<*>.parseErrorJsonResponse(moshi: Moshi): T? {
    val parser = moshi.adapter(T::class.java)
    val response = errorBody()?.string()
    if (response != null)
        try {
            return parser.fromJson(response)
        } catch (e: JsonDataException) {
            e.printStackTrace()
        }
    return null
}

inline fun <reified T> Response<*>.parseJsonResponse(moshi: Moshi): T? {
    val parser = moshi.adapter(T::class.java)
    val response = body()
    if (response != null)
        try {
            return parser.fromJsonValue(response)
        } catch (e: JsonDataException) {
            e.printStackTrace()
        }
    return null
}
