package com.duongnd.ecommerceapp.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.product.ProductItem
import com.duongnd.ecommerceapp.data.repository.ProductsRepository
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {

    val productsList = MutableStateFlow<UiState<MutableList<ProductItem>>>(UiState.Loading)
    val cartList = MutableStateFlow<UiState<Cart>>(UiState.Loading)

    fun getProductById(id: String) = viewModelScope.launch {
        productsRepository.getProductById(id).collect {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    productsList.value = it
                } else {
                    productsList.value = it
                }
            }
        }
    }

    fun addToCart(token: String, addToCartRequest: AddToCartRequest) = viewModelScope.launch {
        productsRepository.addToCart("Bearer $token", addToCartRequest).collect {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    cartList.value = it
                } else {
                    cartList.value = it
                }
            }
        }
    }


}