package com.duongnd.ecommerceapp.viewmodel.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.duongnd.ecommerceapp.data.model.address.AddressItem
import com.duongnd.ecommerceapp.data.model.order.Order
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.utils.MultipleLiveEvent
import com.duongnd.ecommerceapp.utils.SingleLiveEvent

class CheckoutViewModel(private val repository: MyRepository) : ViewModel() {

    val _dataAddressItem = SingleLiveEvent<ArrayList<AddressItem>>()
    val _liveDataAddressItem: LiveData<ArrayList<AddressItem>>
        get() = _dataAddressItem

    val _dataOrder = MultipleLiveEvent<Order>()
    val _liveDataOrder: LiveData<Order>
        get() = _dataOrder

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error


    fun getAllAddresses(token: String, id: String) {
        repository.getAllAddresses(
            "Bearer $token",
            id,
            object : MyRepository.onDataAddressItemListener {
                override fun onDataSuccess(dataProduct: ArrayList<AddressItem>) {
                    _dataAddressItem.value = dataProduct
                }

                override fun onFail(error: String) {
                    _error.value = error
                }
            })
    }

    fun createOrder(token: String, orderItemRequest: OrderItemRequest) {
        repository.createOrder(
             "Bearer $token",
            orderItemRequest,
            object : MyRepository.onDataOrderItemListener {
                override fun onDataSuccess(order: Order) {
                    _dataOrder.value = order
                }

                override fun onDataFail(error: String) {
                    _error.value = error
                }
            })
    }
}