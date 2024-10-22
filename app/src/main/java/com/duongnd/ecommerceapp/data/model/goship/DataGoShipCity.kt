package com.duongnd.ecommerceapp.data.model.goship

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class DataGoShipCity(
    @PrimaryKey val id: String,
    val name: String
)