package com.duongnd.ecommerceapp.data.request

data class AddToCartRequest(
    val userId: String,
    val productId: String,
    val quantity: Int
)