package com.duongnd.ecommerceapp.data.model.user

data class User(
    val _id: String,
    val createdAt: String,
    val email: String,
    val fullName: String,
    val isAdmin: Boolean,
    val updatedAt: String
)