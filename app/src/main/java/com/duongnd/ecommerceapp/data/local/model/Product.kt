package com.duongnd.ecommerceapp.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var _id: String,
//    @field:ColumnInfo(name = "image_urls")
//    var imageUrls: List<ImageUrl>,
    @field:ColumnInfo(name = "name_product")
    var name_product: String,
//    @field:ColumnInfo(name = "price")
//    var price: Any,
)