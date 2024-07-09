package com.duongnd.ecommerceapp.viewmodel.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.MyRepository

class CheckoutViewModelFactory(private val repository: MyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}