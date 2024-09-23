package com.duongnd.ecommerceapp.data.request

data class AddToWishlistRequest(
    val userId: String,
    val productId: String,
)