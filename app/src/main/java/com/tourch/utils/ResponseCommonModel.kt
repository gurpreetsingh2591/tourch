package com.tourch.utils

data class ResponseCommonModel(
    val message: String?,
    val http_code: Int?,
    val data: Any?
)