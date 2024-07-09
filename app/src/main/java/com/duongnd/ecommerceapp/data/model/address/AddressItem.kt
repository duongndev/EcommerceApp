package com.duongnd.ecommerceapp.data.model.address

data class AddressItem(
    val _id: String,
    val addressLine: String,
    val city: String,
    val country: String,
    val createdAt: String,
    val phoneNumber: String,
    val pincode: Int,
    val state: String,
    val title: String,
    val updatedAt: String,
    val userId: String
)