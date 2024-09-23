package com.duongnd.ecommerceapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duongnd.ecommerceapp.data.local.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM products")
   suspend fun getAllProduct(): List<Product>

    @Delete
   suspend fun deleteProduct(product: Product)

}