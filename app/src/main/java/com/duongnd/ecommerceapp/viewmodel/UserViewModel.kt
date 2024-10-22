package com.duongnd.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.user.UsersItem
import com.duongnd.ecommerceapp.data.model.user.address.AddressItem
import com.duongnd.ecommerceapp.data.repository.UsersRepository
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
class UserViewModel @Inject constructor(private val usersRepository: UsersRepository) : ViewModel() {

    private val _addressUser = MutableStateFlow<UiState<List<AddressItem>>>(UiState.Loading)
    val addressUser: StateFlow<UiState<List<AddressItem>>> = _addressUser

    private val _userProfile = MutableStateFlow<UiState<UsersItem>>(UiState.Loading)
    val userProfile: StateFlow<UiState<UsersItem>> = _userProfile


    private val _selectedData = MutableLiveData<AddressItem>()
    val selectedData: LiveData<AddressItem> get() = _selectedData

    fun getAddressUser(token: String) = viewModelScope.launch {
        usersRepository.getAllAddresses(token).collectLatest {
            withContext(Dispatchers.IO){
                if (it is UiState.Success){
                    _addressUser.value = it
                } else {
                    _addressUser.value = it
                }
            }
        }
    }

    fun setSelectedAddress(addressItem: AddressItem) = viewModelScope.launch {
        _selectedData.value = addressItem
    }

    fun getUserProfile(token: String) = viewModelScope.launch {
        usersRepository.getUserProfile(token).collectLatest {
            withContext(Dispatchers.IO){
                if (it is UiState.Success){
                    _userProfile.value = it
                } else {
                    _userProfile.value = it
                }
            }
        }
    }

}