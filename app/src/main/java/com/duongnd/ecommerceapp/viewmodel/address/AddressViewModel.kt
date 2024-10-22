package com.duongnd.ecommerceapp.viewmodel.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AddressViewModel @Inject constructor(
    private val addressRepository: UsersRepository
) : ViewModel() {

    private val _addressItem = MutableStateFlow<UiState<MutableList<AddressItem>>>(UiState.Loading)
    val allAddressItem: StateFlow<UiState<MutableList<AddressItem>>> = _addressItem

    fun getAllAddresses(token: String) = viewModelScope.launch {
        addressRepository.getAllAddresses("Bearer $token").collectLatest { state ->
            withContext(Dispatchers.IO) {
                when (state) {
                    is UiState.Error -> {
                        _addressItem.value = state
                    }

                    UiState.Loading -> {
                        _addressItem.value = UiState.Loading
                    }

                    is UiState.Success -> {
                        _addressItem.value = UiState.Success(state.data)
                    }
                }
            }
        }

    }

}