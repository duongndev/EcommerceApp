package com.duongnd.ecommerceapp.data.model.user

data class UsersItem(
    val _id: String,
    val address: List<Any>,
    val avatarUrl: List<AvatarUrl>,
    val createdAt: String,
    val email: String,
    val fullName: String,
    val password: String,
    val role: String,
    val updatedAt: String
)