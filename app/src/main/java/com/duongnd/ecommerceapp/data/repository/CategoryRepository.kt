package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.category.Category
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
) : ApiResponse() {
    suspend fun getAllCategory(): Flow<Resource<Category>> {
        return flow {
            emit(safeApiCallCategory {
                ecommerceApiService.getAllCategories()
            })
        }.flowOn(Dispatchers.IO)
    }

//    suspend fun getProductById(id: String): Flow<Resource<Products>> {
//        return flow {
//            emit(safeApiCallProducts{
//                ecommerceApiService.getProductById(id)
//            })
//        }.flowOn(Dispatchers.IO)
//    }

}