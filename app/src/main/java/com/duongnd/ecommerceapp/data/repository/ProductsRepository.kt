package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.product.ProductItem
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
) : ApiResponse() {

    val allProducts = mutableListOf<ProductItem>()
    val allCategories = mutableListOf<String>()
    val allCartItems = mutableListOf<Cart>()

    suspend fun getAllProducts(): Flow<UiState<List<ProductItem>>> {
        return flow {
            try {
                val response = ecommerceApiService.getAllProducts()
                if (response.isSuccessful) {
                    response.body()?.let {
                        allProducts.clear()
                        allProducts.addAll(it)
                        emit(UiState.Success(allProducts))
                    }
                    if (allProducts.isNotEmpty()) {
                        emit(UiState.Success(allProducts.toList()))
                    } else {
                        emit(UiState.Error("No data"))
                    }
                } else {
                    emit(UiState.Error("Network call failed with ${response.message()}, ${response.errorBody()}"))
                }

            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllCategories(): Flow<UiState<List<String>>> {
        return flow {
            try {
                val response = ecommerceApiService.getAllCategories()
                if (response.isSuccessful) {
                    response.body()?.let {
                        allCategories.clear()
                        allCategories.addAll(it)
                        emit(UiState.Success(allCategories))
                    }
                    if (allCategories.isNotEmpty()) {
                        emit(UiState.Success(allCategories.toList()))
                    } else {
                        emit(UiState.Error("No data"))
                    }

                } else {
                    emit(UiState.Error("Network call failed with ${response.message()}, ${response.errorBody()}"))
                }

            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getProductById(id: String): Flow<UiState<MutableList<ProductItem>>> {
        return flow {
            try {
                val response = ecommerceApiService.getProductById(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        allProducts.clear()
                        allProducts.add(it)
                        emit(UiState.Success(allProducts))
                    }
                    if (allProducts.isNotEmpty()) {
                        emit(UiState.Success(allProducts))
                    } else {
                        emit(UiState.Error("No data"))
                    }
                } else {
                    emit(UiState.Error("Network call failed with ${response.message()}, ${response.errorBody()}"))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addToCart(token: String, addToCartRequest: AddToCartRequest): Flow<UiState<Cart>> {
        return flow {
            try {
                val response = ecommerceApiService.addCart(token, addToCartRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(UiState.Success(it))
                    }
                } else {
                    emit(UiState.Error("Network call failed with ${response.message()}, ${response.errorBody()}"))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getProductByCategory(category: String): Flow<UiState<List<ProductItem>>> {
        return flow {
            try {
                val response = ecommerceApiService.getProductsByCategoryName(category)
                if (response.isSuccessful) {
                    response.body()?.let {
                        allProducts.clear()
                        allProducts.addAll(it)
                        emit(UiState.Success(allProducts))
                    }
                    if (allProducts.isNotEmpty()) {
                        emit(UiState.Success(allProducts.toList()))
                    } else {
                        emit(UiState.Error("No data"))
                    }
                } else {
                    emit(UiState.Error("Network call failed with ${response.message()}, ${response.errorBody()}"))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}