package com.fightpandemics.core.data.model.login

data class LoginResponse(
    val email: String,
    val emailVerified: Boolean,
    val token: String,
    val user: Any
) : ErrorResponse()