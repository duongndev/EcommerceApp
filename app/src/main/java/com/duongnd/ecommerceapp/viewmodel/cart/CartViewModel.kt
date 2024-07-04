package com.duongnd.ecommerceapp.viewmodel.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import com.duongnd.ecommerceapp.utils.MultipleLiveEvent

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    val _dataCart = MultipleLiveEvent<Cart>()
    val _liveDataCart: LiveData<Cart>
        get() = _dataCart

    private val _error = MultipleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    val _dataCartIncrement = MultipleLiveEvent<Cart>()
    val _liveDataCartIncrement: LiveData<Cart>
        get() = _dataCartIncrement

    val _dataCartDecrement = MultipleLiveEvent<Cart>()
    val _liveDataCartDecrement: LiveData<Cart>
        get() = _dataCartDecrement


    fun getUserCart(id: String, token: String) {
        cartRepository.getUserCart(id, "Bearer $token", object : CartRepository.onDataCartListener {
            override fun onDataSuccess(cart: Cart) {
                _dataCart.value = cart
            }

            override fun onDataFail(error: String) {
                _error.value = error
            }
        })
    }

    fun incrementQuantity(id: String, token: String, cartItemRequest: CartItemRequest) {
        cartRepository.incrementQuantity(
            id,
            "Bearer $token",
            cartItemRequest,
            object : CartRepository.onDataCartListener {
                override fun onDataSuccess(cart: Cart) {
                    _dataCartIncrement.value = cart
                }

                override fun onDataFail(error: String) {
                    _error.value = error
                }
            })
    }

    fun decrementQuantity(id: String, token: String, cartItemRequest: CartItemRequest) {
        cartRepository.decrementQuantity(
            id,
            "Bearer $token",
            cartItemRequest,
            object : CartRepository.onDataCartListener {
                override fun onDataSuccess(cart: Cart) {
                    _dataCartDecrement.value = cart
                }

                override fun onDataFail(error: String) {
                    _error.value = error
                }
            })
    }
}