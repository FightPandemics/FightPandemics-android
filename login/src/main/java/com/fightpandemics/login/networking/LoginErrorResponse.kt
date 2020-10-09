package com.fightpandemics.login.networking

data class LoginErrorResponse(
    val error: String,
    val message: String,
    val statusCode: Int
)
