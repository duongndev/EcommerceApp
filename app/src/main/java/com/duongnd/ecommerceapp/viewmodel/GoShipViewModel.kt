package com.duongnd.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.BuildConfig
import com.duongnd.ecommerceapp.data.model.carrier.CarrierData
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipDistricts
import com.duongnd.ecommerceapp.data.repository.GoShipRepository
import com.duongnd.ecommerceapp.data.request.goship.GoShipRequest
import com.duongnd.ecommerceapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GoShipViewModel @Inject constructor(
    private val goShipRepository: GoShipRepository
) : ViewModel() {
    private val _citiesList = MutableStateFlow<UiState<List<DataGoShipCity>>>(UiState.Loading)
    val citiesState: StateFlow<UiState<List<DataGoShipCity>>> get() = _citiesList

    private val _districts = MutableLiveData<UiState<List<DataGoShipDistricts>>>()
    val districts: LiveData<UiState<List<DataGoShipDistricts>>> = _districts

    private val _carriers = MutableLiveData<UiState<List<CarrierData>>>()
    val carriers:  LiveData<UiState<List<CarrierData>>> = _carriers

    private val token = BuildConfig.SANBOX_GOSHIP_TOKEN


    private val mutableSelectedItem = MutableLiveData<CarrierData>()
    val selectedItem: LiveData<CarrierData> get() = mutableSelectedItem


    fun getAllCities() = viewModelScope.launch {
        goShipRepository.getAllCities(token).collectLatest {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    _citiesList.value = it
                } else {
                    _citiesList.value = it
                }
            }
        }
    }

    fun getAllDistricts(cityId: String) = viewModelScope.launch {
        goShipRepository.getAllDistricts(token, cityId).collect {
            _districts.postValue(it)
        }
    }

    fun getAllCarriers(goShipRequest: GoShipRequest) = viewModelScope.launch {
        goShipRepository.getAllCarriers(token, goShipRequest).collectLatest {
            _carriers.postValue(it)
        }
    }


    fun setSelectedCarrier(carrierData: CarrierData) {
        mutableSelectedItem.value = carrierData
    }



}