package com.duongnd.ecommerceapp.data.api

import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.utils.Resource
import retrofit2.Response
import timber.log.Timber

abstract class ApiResponse {

    suspend fun getAllProducts(
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
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun error(errorMessage: String): Resource<Products> =
        Resource.Error("Api call failed $errorMessage")
}