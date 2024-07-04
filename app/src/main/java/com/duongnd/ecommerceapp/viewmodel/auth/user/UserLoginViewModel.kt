package com.duongnd.ecommerceapp.viewmodel.auth.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.duongnd.ecommerceapp.data.repository.AuthRepository
import com.duongnd.ecommerceapp.data.model.user.User
import com.duongnd.ecommerceapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserLoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val _dataUser = SingleLiveEvent<User>()
    val _liveDataUser: LiveData<User>
        get() = _dataUser

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error


    fun getUserLogin(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authRepository.getUser("Bearer $token", object : AuthRepository.onDataUserListener {
                override fun onSuccess(user: User) {
                    _dataUser.postValue(user)
                }

                override fun onFail(error: String) {
                    _error.postValue(error)
                }
            })
        }
    }
}