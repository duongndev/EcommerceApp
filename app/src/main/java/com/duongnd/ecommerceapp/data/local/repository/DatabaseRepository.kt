package com.duongnd.ecommerceapp.data.local.repository

import com.duongnd.ecommerceapp.data.local.dao.ProductDao
import com.duongnd.ecommerceapp.data.local.model.Product

class DatabaseRepository(
    private val productDao: ProductDao
) {
    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun getAllProduct(): List<Product> {
        return productDao.getAllProduct()
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }
}