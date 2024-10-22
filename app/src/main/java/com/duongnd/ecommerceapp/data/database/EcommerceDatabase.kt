package com.duongnd.ecommerceapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipDistricts

@Database(entities = [DataGoShipCity::class, DataGoShipDistricts::class], version = 1)
abstract class EcommerceDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}