package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductsRepository @Inject constructor(

    private val apiService: ApiService
): ApiResponse() {
    suspend fun getAllProducts(): Flow<Resource<Products>> {
        return flow {
            emit(getAllProducts {
                apiService.getAllProducts2()
            })
        }.flowOn(Dispatchers.IO)
    }
}