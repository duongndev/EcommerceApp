package com.duongnd.ecommerceapp.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class ProductTypeConverter {
    @TypeConverter
    fun fromProductModel(vales:ImageUrl): String = Gson().toJson(vales)

    @TypeConverter
    fun toProductModel(vales: String): ImageUrl = Gson().fromJson(vales, ImageUrl::class.java)
}