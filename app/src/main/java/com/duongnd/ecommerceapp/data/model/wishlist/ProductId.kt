package com.duongnd.ecommerceapp.data.model.wishlist

data class ProductId(
    val _id: String,
    val imageUrls: List<ImageUrl>,
    val name_product: String,
    val price: Double
)