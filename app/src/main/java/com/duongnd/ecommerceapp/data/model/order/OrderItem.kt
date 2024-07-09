package com.duongnd.ecommerceapp.data.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val _id: String,
    val image: String,
    val name: String,
    val price: Int,
    val productId: String,
    val quantity: Int
) : Parcelable