package com.duongnd.ecommerceapp.viewmodel.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.address.Address
import com.duongnd.ecommerceapp.data.repository.AddressRepository
import com.duongnd.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
):ViewModel() {

    private val _addressItem: MutableLiveData<Address> = MutableLiveData()
    val allAddressItem: LiveData<Address> = _addressItem

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    fun getAllAddresses(token: String, userId: String) = viewModelScope.launch {
        addressRepository.getAllAddresses("Bearer $token", userId).collect { response ->
            when (response) {
                is Resource.Success -> {
                    _loading.value = false
                    val data = response.data!!
                    _addressItem.postValue(data)
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