package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.carrier.CarrierData
import com.duongnd.ecommerceapp.data.model.order.DataOrder
import com.duongnd.ecommerceapp.data.model.user.address.Address
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.data.request.goship.GoShipRequest
import com.duongnd.ecommerceapp.data.request.shipping.ShippingRequest
import com.duongnd.ecommerceapp.utils.Resource
import com.duongnd.ecommerceapp.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CheckoutRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService,
) : ApiResponse() {


    val allCarrierItems = mutableListOf<CarrierData>()

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


    suspend fun getAllShippingFee(shippingRequest: ShippingRequest): Flow<UiState<List<CarrierData>>> {
        return flow {
            try {
                val response = ecommerceApiService.getShippingFee(shippingRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        allCarrierItems.clear()
                        allCarrierItems.addAll(it.data)
                        emit(UiState.Success(allCarrierItems))
                    }
                } else {
                    emit(UiState.Error(response.message().toString()))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}