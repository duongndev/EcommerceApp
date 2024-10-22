package com.duongnd.ecommerceapp.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.model.user.UsersItem
import com.duongnd.ecommerceapp.data.repository.AuthRepository
import com.duongnd.ecommerceapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _dataLogin = MutableStateFlow<UiState<DataLogin>>(UiState.Loading)
    val dataLogin: StateFlow<UiState<DataLogin>> = _dataLogin

    private val _dataUser = MutableStateFlow<UiState<UsersItem>>(UiState.Loading)
    val dataUser: StateFlow<UiState<UsersItem>> = _dataUser

    fun loginUser(loginRequest: LoginRequest) = viewModelScope.launch {
        authRepository.getLogin(loginRequest).collectLatest {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    _dataLogin.value = it
                } else {
                    _dataLogin.value = it
                }
            }
        }
    }


    fun checkUser(token: String) = viewModelScope.launch {
        authRepository.checkUser(token).collectLatest {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    _dataUser.value = it
                } else {
                    _dataUser.value = it
                }
            }
        }
    }


}