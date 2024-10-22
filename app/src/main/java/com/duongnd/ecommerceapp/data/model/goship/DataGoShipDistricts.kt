package com.duongnd.ecommerceapp.data.model.goship

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "districts")
data class DataGoShipDistricts(
    @PrimaryKey val id: String,
    val city_id: String,
    val name: String
)