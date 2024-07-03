package com.duongnd.ecommerceapp.data.model.login

data class UserInfo(
    val _id: String,
    val avatar: String,
    val bookings: List<Any>,
    val createdAt: String,
    val email: String,
    val fullName: String,
    val isAdmin: Boolean,
    val phoneNumber: String,
    val updatedAt: String
)