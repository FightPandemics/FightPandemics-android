package com.fightpandemics.login.networking

data class SignUpRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)