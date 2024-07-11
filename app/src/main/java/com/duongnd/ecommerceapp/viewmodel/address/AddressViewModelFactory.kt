package com.duongnd.ecommerceapp.viewmodel.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.AddressRepository

class AddressViewModelFactory(private val addressRepository: AddressRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddressViewModel(addressRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}