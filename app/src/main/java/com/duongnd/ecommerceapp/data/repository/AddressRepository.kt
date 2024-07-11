package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.address.Address
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddressRepository  @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
): ApiResponse(){
    suspend fun getAllAddresses(token: String, userId: String): Flow<Resource<Address>> {
        return flow {
            emit(safeApiCallAddress {
                ecommerceApiService.getAllAddresses(token, userId)
            })
        }.flowOn(Dispatchers.IO)
    }
}