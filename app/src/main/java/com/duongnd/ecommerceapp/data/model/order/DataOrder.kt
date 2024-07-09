package com.duongnd.ecommerceapp.data.model.order

data class DataOrder(
    val _id: String,
    val createdAt: String,
    val orderItems: List<OrderItem>,
    val paymentMethod: String,
    val phoneNumber: String,
    val shippingAddress: String,
    val status: String,
    val totalAmount: Int,
    val updatedAt: String,
    val userId: String
)