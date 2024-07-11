package com.duongnd.ecommerceapp.data.request

data class OrderItemRequest (
    val userId: String,
    val paymentMethod: String,
    val addressLine: String,
    val phoneNumber: String,
)