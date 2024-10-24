package com.duongnd.ecommerceapp.viewmodel.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import com.duongnd.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _cartItems: MutableLiveData<Cart> = MutableLiveData()
    val cartItems: LiveData<Cart> = _cartItems

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage


    fun getUserCart(token: String) = viewModelScope.launch {
        cartRepository.getUsersCart("Bearer $token").collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _cartItems.postValue(data)
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

    fun incrementQuantityCart(id: String, token: String, cartItemRequest: CartItemRequest) =
        viewModelScope.launch {
            cartRepository.incrementQuantityCart(id, "Bearer $token", cartItemRequest)
                .collect { response ->
                    run {
                        when (response) {
                            is Resource.Success -> {
                                _loading.value = false
                                val data = response.data!!
                                _cartItems.postValue(data)
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

    fun decrementQuantityCart(id: String, token: String, cartItemRequest: CartItemRequest) =
        viewModelScope.launch {
            cartRepository.decrementQuantityCart(id, "Bearer $token", cartItemRequest)
                .collect { response ->
                    run {
                        when (response) {
                            is Resource.Success -> {
                                _loading.value = false
                                val data = response.data!!
                                _cartItems.postValue(data)
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
}