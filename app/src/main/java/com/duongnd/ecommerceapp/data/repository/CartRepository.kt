package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
): ApiResponse() {
    suspend fun getUsersCart(id: String, token: String): Flow<Resource<Cart>> {
        return flow {
            emit(safeApiCallCart {
                ecommerceApiService.getUserCart(id, token)
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