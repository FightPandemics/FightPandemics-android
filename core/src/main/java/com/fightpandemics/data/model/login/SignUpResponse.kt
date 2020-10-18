package com.fightpandemics.data.model.login

data class SignUpResponse(
    val emailVerified: Boolean,
    val token: String
) : ErrorResponse()