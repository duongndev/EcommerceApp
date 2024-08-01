package com.duongnd.ecommerceapp.data.model.product

data class DataProduct(
    val _id: String,
    val categoryId: CategoryId,
    val countInStock: Int,
    val createdAt: String,
    val description: String,
    val imageUrls: List<ImageUrl>,
    val name_product: String,
    val price: Any,
    val size: List<Any>,
    val colors: List<Any>,
    val reviews: List<Review>,
    val updatedAt: String
)