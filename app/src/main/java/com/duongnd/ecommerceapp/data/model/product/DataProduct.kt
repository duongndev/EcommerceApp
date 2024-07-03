package com.duongnd.ecommerceapp.data.model.product

data class DataProduct(
    val _id: String,
    val categoryId: String,
    val countInStock: Int,
    val createdAt: String,
    val description: String,
    val imageUrls: List<ImageUrl>,
    val name_product: String,
    val price: Int,
    val reviews: List<Any>,
    val updatedAt: String
)