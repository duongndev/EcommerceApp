package com.duongnd.ecommerceapp.data.api

import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.category.Category
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.utils.Resource
import retrofit2.Response
import timber.log.Timber

abstract class ApiResponse {

    suspend fun safeApiCallProducts(
        apiCall: suspend () -> Response<Products>
    ): Resource<Products> {
        try {
            Timber.tag("ApiResponse").d("checking...")
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.Success(body)
                }
            }
            return errorProduct("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return errorProduct(e.message ?: e.toString())
        }
    }

    suspend fun safeApiCallProductsDetail(
        apiCall: suspend () -> Response<DataProduct>
    ): Resource<DataProduct> {
        try {
            Timber.tag("ApiResponse").d("checking...")
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.Success(body)
                }
            }
            return errorProductDetail("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return errorProductDetail(e.message ?: e.toString())
        }
    }

    suspend fun safeApiCallCategory(
        apiCall: suspend () -> Response<Category>
    ): Resource<Category> {
        try {
            Timber.tag("ApiResponse").d("checking...")
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.Success(body)
                }
            }
            return errorCategory("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return errorCategory(e.message ?: e.toString())
        }
    }

    suspend fun safeApiCallCart(
        apiCall: suspend () -> Response<Cart>
    ): Resource<Cart> {
        try {
            Timber.tag("ApiResponse").d("checking...")
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.Success(body)
                }
            }
            return errorCart("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return errorCart(e.message ?: e.toString())
        }
    }


    private fun errorProduct(errorMessage: String): Resource<Products> =
        Resource.Error("Api call failed $errorMessage")

    private fun errorProductDetail(errorMessage: String): Resource<DataProduct> =
        Resource.Error("Api call failed $errorMessage")

    private fun errorCategory(errorMessage: String): Resource<Category> =
        Resource.Error("Api call failed $errorMessage")

    private fun errorCart(errorMessage: String): Resource<Cart> =
        Resource.Error("Api call failed $errorMessage")
}