package com.duongnd.ecommerceapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.duongnd.ecommerceapp.data.local.dao.ProductDao
import com.duongnd.ecommerceapp.data.local.model.Product
import com.duongnd.ecommerceapp.data.local.model.ProductTypeConverter

@Database(entities = [Product::class], version = 1, exportSchema = true)
@TypeConverters(ProductTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}