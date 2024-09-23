package com.duongnd.ecommerceapp.data.model.wishlist

data class WishlistItem(
    val _id: String,
    val createdAt: String,
    val productId: ProductId,
    val updatedAt: String,
    val userId: String
)