package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
) : ApiResponse() {

    suspend fun getLogin(loginRequest: LoginRequest): Flow<Resource<DataLogin>> {
        return flow {
            emit(safeApiCallLogin {
                ecommerceApiService.loginUser(loginRequest)
            })
        }.flowOn(Dispatchers.IO)
    }

}