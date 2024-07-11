package com.duongnd.ecommerceapp.viewmodel.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.repository.AuthRepository
import com.duongnd.ecommerceapp.utils.MultipleLiveEvent
import com.duongnd.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _dataLogin: MultipleLiveEvent<DataLogin> = MultipleLiveEvent()
    val dataLogin: LiveData<DataLogin> = _dataLogin

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage


    fun loginUser(loginRequest: LoginRequest) = viewModelScope.launch {
        authRepository.getLogin(loginRequest).collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _dataLogin.postValue(data)
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