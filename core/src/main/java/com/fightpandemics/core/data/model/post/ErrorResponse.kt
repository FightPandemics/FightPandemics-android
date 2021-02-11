package com.fightpandemics.core.data.model.post

data class ErrorResponse(
    val statusCode: Int?,
    val error: String?,
    val message: String?
)