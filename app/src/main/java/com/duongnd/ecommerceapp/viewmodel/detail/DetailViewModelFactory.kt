package com.duongnd.ecommerceapp.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.viewmodel.home.HomeViewModel

class DetailViewModelFactory(private val repository: MyRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}