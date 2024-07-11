package com.duongnd.ecommerceapp.viewmodel.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.CheckoutRepository
import com.duongnd.ecommerceapp.data.repository.MyRepository

class CheckoutViewModelFactory(private val checkoutRepository: CheckoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(checkoutRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}