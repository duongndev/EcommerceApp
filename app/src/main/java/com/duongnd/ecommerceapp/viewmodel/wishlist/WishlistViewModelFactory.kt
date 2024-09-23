package com.duongnd.ecommerceapp.viewmodel.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.data.repository.WishlistRepository
import com.duongnd.ecommerceapp.viewmodel.home.HomeViewModel


class WishlistViewModelFactory(private val wishlistRepository: WishlistRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WishlistViewModel(wishlistRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}