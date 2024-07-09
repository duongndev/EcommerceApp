package com.duongnd.ecommerceapp.data.model.order

data class Order(
    val `data`: DataOrder,
    val message: String,
    val status: String
)