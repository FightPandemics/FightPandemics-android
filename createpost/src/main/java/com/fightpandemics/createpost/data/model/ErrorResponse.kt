package com.fightpandemics.createpost.data.model

data class ErrorResponse(
    val statusCode: Int?,
    val error: String?,
    val message: String?
)