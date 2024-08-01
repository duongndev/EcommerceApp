package com.duongnd.ecommerceapp.data.model.product

data class Review(
    val _id: String,
    val rating: Any,
    val comment: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val productId: String,
)