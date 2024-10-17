package com.duongnd.ecommerceapp.viewmodel.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.BuildConfig
import com.duongnd.ecommerceapp.data.model.address.Address
import com.duongnd.ecommerceapp.data.model.goship.GoShipCity
import com.duongnd.ecommerceapp.data.model.order.DataOrder
import com.duongnd.ecommerceapp.data.repository.CheckoutRepository
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository
) : ViewModel() {


    private val _orderItem: MutableLiveData<DataOrder> = MutableLiveData()
    val orderItem: LiveData<DataOrder> = _orderItem

    private val _addressItem: MutableLiveData<Address> = MutableLiveData()
    val addressItem: LiveData<Address> = _addressItem

    private val _itemCity: MutableLiveData<GoShipCity> = MutableLiveData()
    val cityItemData: LiveData<GoShipCity> = _itemCity

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage


    val tokenGoShip = BuildConfig.SANBOX_GOSHIP_TOKEN

    fun getOrder(token: String, orderItemRequest: OrderItemRequest) = viewModelScope.launch {
        checkoutRepository.createOrder("Bearer $token", orderItemRequest).collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _orderItem.postValue(data)
                        Timber.d("loadProductsList: $data")
                    }

                    is Resource.Error -> {
                        _loading.value = false
                        _errorMessage.postValue(response.message!!)
                    }

                    is Resource.Loading -> {
                        _loading.value = true
                    }
                }
            }
        }
    }

    fun getALlAddresses(token: String, id: String) = viewModelScope.launch {
        checkoutRepository.getUserAddress("Bearer $token", id).collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _addressItem.postValue(data)
                        Timber.d("loadAddresses: $data")
                    }

                    is Resource.Error -> {
                        _loading.value = false
                        _errorMessage.postValue(response.message!!)
                    }

                    is Resource.Loading -> {
                        _loading.value = true
                    }
                }
            }
        }
    }


}