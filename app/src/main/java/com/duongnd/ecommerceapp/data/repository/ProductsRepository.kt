package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
) : ApiResponse() {
    suspend fun getAllProducts(): Flow<Resource<Products>> {
        return flow {
            emit(safeApiCallProducts {
                ecommerceApiService.getAllProducts()
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProductById(id: String): Flow<Resource<DataProduct>> {
        return flow {
            emit(safeApiCallProductsDetail{
                ecommerceApiService.getProductById(id)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProductsByCategoryName(name: String): Flow<Resource<Products>> {
        return flow {
            emit(safeApiCallProducts{
                ecommerceApiService.getProductsByCategoryName(name)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addToCart(token: String, addToCartRequest: AddToCartRequest): Flow<Resource<Cart>> {
        return flow {
            emit(safeApiCallCart {
                ecommerceApiService.addCart(token, addToCartRequest)
            })
        }.flowOn(Dispatchers.IO)
    }

}