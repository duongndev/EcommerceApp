package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.GoShipService
import com.duongnd.ecommerceapp.data.database.CityDao
import com.duongnd.ecommerceapp.data.model.carrier.CarrierData
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipDistricts
import com.duongnd.ecommerceapp.data.request.goship.GoShipRequest
import com.duongnd.ecommerceapp.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GoShipRepository @Inject constructor(
    private val goShipService: GoShipService,
    private val cityDao: CityDao
) {

    val allCities = mutableListOf<DataGoShipCity>()
    val allDistricts = mutableListOf<DataGoShipDistricts>()

    val allCarrierItems = mutableListOf<CarrierData>()

    suspend fun getAllCities(token: String): Flow<UiState<List<DataGoShipCity>>> {
        return flow {
            val cachedCities = cityDao.getAllCities().firstOrNull()
            if (cachedCities != null && cachedCities.isNotEmpty()) {
                emit(UiState.Success(cachedCities))
            }
            try {
                val response = goShipService.getAllCities("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        allCities.addAll(it.data)
                        cityDao.insertCities(it.data)
                        emit(UiState.Success(allCities))
                    }
                    if (allCities.isNotEmpty()) {
                        cityDao.insertCities(allCities)
                        emit(UiState.Success(allCities.toList()))
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

    suspend fun getAllDistricts(
        token: String,
        cityId: String
    ): Flow<UiState<List<DataGoShipDistricts>>> {
        return flow {
            try {
                val response = goShipService.getAllCityDistricts("Bearer $token", cityId)
                val cachedDistricts = cityDao.getDistrictsByCityId(cityId).firstOrNull()
                if (cachedDistricts != null && cachedDistricts.isNotEmpty()) {
                    emit(UiState.Success(cachedDistricts))
                }
                if (response.isSuccessful) {
                    response.body()?.let {
                        allDistricts.clear()
                        allDistricts.addAll(it.data)
                        cityDao.insertDistricts(it.data)
                        emit(UiState.Success(allDistricts))
                    }
                    if (allDistricts.isNotEmpty()) {
                        cityDao.insertDistricts(allDistricts)
                        emit(UiState.Success(allDistricts.toList()))
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


    suspend fun getAllCarriers(token: String, goShipRequest: GoShipRequest): Flow<UiState<List<CarrierData>>> {
        return flow {
            try {
                val response = goShipService.getRates("Bearer $token", goShipRequest)
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