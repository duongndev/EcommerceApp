package com.duongnd.ecommerceapp.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUrl(
    val public_id: String,
    val secure_url: String
): java.io.Serializable, Parcelable