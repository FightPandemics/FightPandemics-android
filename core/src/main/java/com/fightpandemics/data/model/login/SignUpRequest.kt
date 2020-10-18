package com.fightpandemics.data.model.login

data class SignUpRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)