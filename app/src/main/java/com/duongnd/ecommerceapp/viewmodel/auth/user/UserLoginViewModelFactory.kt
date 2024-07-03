package com.duongnd.ecommerceapp.viewmodel.auth.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.AuthRepository
import com.duongnd.ecommerceapp.data.repository.MyRepository

class UserLoginViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserLoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserLoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}