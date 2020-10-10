package com.fightpandemics.login.networking

data class SignUpResponse(
    val emailVerified: Boolean,
    val token: String
) : BaseResponse()