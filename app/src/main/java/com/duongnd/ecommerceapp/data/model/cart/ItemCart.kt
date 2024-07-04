package com.duongnd.ecommerceapp.data.model.cart

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemCart(
    val _id: String,
    val image: String,
    val name: String,
    val price: Int,
    val productId: String,
    val quantity: Int
) : Parcelable