package com.duongnd.ecommerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.GoShipRepository

class GoShipViewModelFactory(private val goShipRepository: GoShipRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoShipViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoShipViewModel(goShipRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}