package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.user.UsersItem
import com.duongnd.ecommerceapp.data.model.user.address.Address
import com.duongnd.ecommerceapp.data.model.user.address.AddressItem
import com.duongnd.ecommerceapp.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
) {
    val usersAddress = mutableListOf<AddressItem>()
    val userDetails = mutableListOf<UsersItem>()

    suspend fun getAllAddresses(token: String): Flow<UiState<MutableList<AddressItem>>> {
        return flow {
            try {
                val response = ecommerceApiService.getAllAddresses("Bearer $token")
                if (response.isSuccessful){
                    response.body()?.let {
                        usersAddress.addAll(it)
                        emit(UiState.Success(usersAddress))
                    }
                    if (usersAddress.isNotEmpty()){
                        emit(UiState.Success(usersAddress))
                    } else {
                        emit(UiState.Error("No data available"))
                    }
                } else {
                    emit(UiState.Error(response.message().toString()))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserProfile(token: String): Flow<UiState<UsersItem>> {
        return flow {
            try {
                val response = ecommerceApiService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        userDetails.add(it)
                        emit(UiState.Success(userDetails[0]))
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