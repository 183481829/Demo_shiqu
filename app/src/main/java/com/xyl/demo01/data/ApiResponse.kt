package com.xyl.demo01.data

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T
)
