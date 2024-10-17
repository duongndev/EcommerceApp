package com.duongnd.ecommerceapp.data.request

data class AddToCartRequest(
    val productId: String,
    val quantity: Int
)