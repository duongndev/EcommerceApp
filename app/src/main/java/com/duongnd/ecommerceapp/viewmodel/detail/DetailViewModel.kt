package com.duongnd.ecommerceapp.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.utils.SingleLiveEvent

class DetailViewModel(private val repository: MyRepository) : ViewModel() {

    val _dataProductItem = SingleLiveEvent<ArrayList<DataProduct>>()
    val _liveDataProductItem: LiveData<ArrayList<DataProduct>>
        get() = _dataProductItem

    val _dataProductDetail = SingleLiveEvent<DataProduct>()
    val _liveDataProductDetail: LiveData<DataProduct>
        get() = _dataProductDetail


    val _dataCart = SingleLiveEvent<Cart>()
    val _liveDataCart: LiveData<Cart>
        get() = _dataCart


    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    fun geDataProductById(id: String){
        repository.getProductsById(id, object : MyRepository.onDataProductDetailListener {
            override fun onDataSuccess(dataProduct: DataProduct) {
                _dataProductDetail.value = dataProduct
            }

            override fun onDataFail(error: String) {
                _error.value = error
            }

        })
    }

    fun addToCart(token: String, addToCartRequest: AddToCartRequest){
        repository.addToCart("Bearer $token", addToCartRequest, object : MyRepository.onDataCartListener {
            override fun onDataSuccess(car: Cart) {
                _dataCart.value = car
            }

            override fun onDataFail(error: String) {
                _error.value = error
            }

        })
    }

}