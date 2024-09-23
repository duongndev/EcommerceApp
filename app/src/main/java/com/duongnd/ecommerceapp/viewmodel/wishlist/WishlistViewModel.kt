package com.duongnd.ecommerceapp.viewmodel.wishlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.wishlist.Wishlist
import com.duongnd.ecommerceapp.data.repository.WishlistRepository
import com.duongnd.ecommerceapp.utils.MultipleLiveEvent
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private val _wishlistItems: MultipleLiveEvent<Wishlist> = MultipleLiveEvent()
    val wishlistItems: LiveData<Wishlist> = _wishlistItems

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage


    fun getUserWishlist(token: String, userId: String) = viewModelScope.launch {
        wishlistRepository.getUsersWishlist("Bearer $token", userId).collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _wishlistItems.postValue(data)
                        Log.d("wishlistViewModel", "loadProductsList: $data")
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