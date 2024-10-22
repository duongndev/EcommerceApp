package com.duongnd.ecommerceapp.viewmodel.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.BuildConfig
import com.duongnd.ecommerceapp.data.model.carrier.CarrierData
import com.duongnd.ecommerceapp.data.model.goship.GoShipCity
import com.duongnd.ecommerceapp.data.model.order.DataOrder
import com.duongnd.ecommerceapp.data.model.user.address.Address
import com.duongnd.ecommerceapp.data.repository.CheckoutRepository
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.data.request.goship.GoShipRequest
import com.duongnd.ecommerceapp.data.request.shipping.ShippingRequest
import com.duongnd.ecommerceapp.utils.Resource
import com.duongnd.ecommerceapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _carriers = MutableStateFlow<UiState<List<CarrierData>>>(UiState.Loading)
    val carriers:  StateFlow<UiState<List<CarrierData>>> get() = _carriers


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


    fun getAllShippingFee(shippingRequest: ShippingRequest) = viewModelScope.launch {
        checkoutRepository.getAllShippingFee(shippingRequest).collectLatest {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    _carriers.value = it
                } else {
                    _carriers.value = it
                }
            }
        }
    }

}