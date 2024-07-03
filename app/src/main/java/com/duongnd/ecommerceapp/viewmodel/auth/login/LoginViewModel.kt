package com.duongnd.ecommerceapp.viewmodel.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.repository.AuthRepository
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val _dataLogin = SingleLiveEvent<DataLogin>()
    val _liveDataLogin: LiveData<DataLogin>
        get() = _dataLogin

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error


    fun getLogin(login: LoginRequest) {
        viewModelScope.launch {
            authRepository.getLogin(login, object: AuthRepository.onDataLoginListener {
                override fun onSuccess(dataLogin: DataLogin) {
                    _dataLogin.value = dataLogin
                }

                override fun onFail(error: String) {
                    _error.value = error
                }
            })
        }
    }
}