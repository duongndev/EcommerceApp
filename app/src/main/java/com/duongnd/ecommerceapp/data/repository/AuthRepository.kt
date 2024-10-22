package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.model.user.UsersItem
import com.duongnd.ecommerceapp.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
) {

    suspend fun getLogin(loginRequest: LoginRequest): Flow<UiState<DataLogin>> {
        return flow {
            try {
                val response = ecommerceApiService.loginUser(loginRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(UiState.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun checkUser(token: String): Flow<UiState<UsersItem>> {
        return flow {
            try {
                val response = ecommerceApiService.checkUser("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(UiState.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}