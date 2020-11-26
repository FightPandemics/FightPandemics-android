package com.fightpandemics.core.data.model.login

open class ErrorResponse(
    val statusCode: Int?,
    val error: String?,
    val message: String?
)
