package com.fightpandemics.login.data.model


data class ChangePasswordModel(
    val email: String,
    val responseMessage: String
) : BaseResponse()