package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.address.Address
import com.duongnd.ecommerceapp.data.model.order.DataOrder
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CheckoutRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService,
) : ApiResponse() {

    suspend fun createOrder(
        token: String,
        orderItemRequest: OrderItemRequest
    ): Flow<Resource<DataOrder>> {
        return flow {
            emit(safeApiCallOrder {
                ecommerceApiService.addOrder(token, orderItemRequest)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserAddress(token: String, id: String): Flow<Resource<Address>> {
        return flow {
            emit(safeApiCallAddress{
                ecommerceApiService.getAllAddresses(token, id)
            })
        }.flowOn(Dispatchers.IO)
    }

}