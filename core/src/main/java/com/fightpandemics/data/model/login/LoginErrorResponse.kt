package com.fightpandemics.data.model.login

data class LoginErrorResponse(
    val error: String,
    val message: String,
    val statusCode: Int
)
