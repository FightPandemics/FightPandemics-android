package com.fightpandemics.login.networking

data class ChangePasswordResponse(
    val email: String,
    val responseMessage: String
) : ErrorResponse()