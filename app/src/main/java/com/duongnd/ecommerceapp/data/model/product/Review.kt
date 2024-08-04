package com.duongnd.ecommerceapp.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val _id: String,
    val rating: Double,
    val comment: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val productId: String,
): Parcelable