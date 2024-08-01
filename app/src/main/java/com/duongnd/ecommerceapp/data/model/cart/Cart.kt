package com.duongnd.ecommerceapp.data.model.cart

data class Cart(
    val _id: String,
    val createdAt: String,
    val itemsCart: ArrayList<ItemCart>,
    val totalAmount: Any,
    val updatedAt: String,
    val userId: String
)