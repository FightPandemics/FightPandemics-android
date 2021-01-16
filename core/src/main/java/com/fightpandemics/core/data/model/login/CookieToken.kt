package com.fightpandemics.core.data.model.login

import com.squareup.moshi.Json

data class CookieToken(
    @field:Json(name = "remember") val remember: String = "-",
    @field:Json(name = "token") var token: String?
){
    fun getCookieParameter() : String = "remember=-;token=$token"

    fun getCookieName ():String = "cookie"
}



