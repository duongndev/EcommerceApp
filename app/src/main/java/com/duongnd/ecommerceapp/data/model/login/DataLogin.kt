package com.duongnd.ecommerceapp.data.model.login

data class DataLogin(
    val userId: String,
    val token: String,
    val role: String,
    val message: String,
    val success: Boolean
)