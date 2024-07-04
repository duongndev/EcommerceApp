package com.duongnd.ecommerceapp.viewmodel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.viewmodel.home.HomeViewModel

class CartViewModelFactory(private val cartRepository: CartRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}