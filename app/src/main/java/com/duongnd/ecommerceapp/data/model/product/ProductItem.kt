package com.duongnd.ecommerceapp.data.model.product

data class ProductItem(
    val _id: String,
    val category: String,
    val colors: List<Any>,
    val countInStock: Int,
    val createdAt: String,
    val imageUrls: List<ImageUrl>,
    val product_desc: String,
    val product_name: String,
    val product_price: Int,
    val reviews: List<Any>,
    val size: List<Any>,
    val updatedAt: String
)