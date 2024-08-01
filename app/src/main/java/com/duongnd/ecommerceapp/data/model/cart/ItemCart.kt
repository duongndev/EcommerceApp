package com.duongnd.ecommerceapp.data.model.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemCart(
    val _id: String,
    val image: String,
    val name: String,
    val price: Double,
    val productId: String,
    val quantity: Int
) : Parcelable