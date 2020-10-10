package com.fightpandemics.login.networking

data class LoginResponse(
    val email: String,
    val emailVerified: Boolean,
    val token: String,
    val user: Any
) : BaseResponse()