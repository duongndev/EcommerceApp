package com.duongnd.ecommerceapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.product.ProductItem
import com.duongnd.ecommerceapp.data.repository.ProductsRepository
import com.duongnd.ecommerceapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewHomeViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    val productsList = MutableStateFlow<UiState<List<ProductItem>>>(UiState.Loading)
    val categoriesList = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val productsCategoryList = MutableStateFlow<UiState<List<ProductItem>>>(UiState.Loading)
//    init {
//        loadProductsList()
//        productsList.value = UiState.Loading
//        loadCategories()
//        categoriesList.value = UiState.Loading
//    }

    fun loadProductsList() = viewModelScope.launch {
        productsRepository.getAllProducts().collect {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    productsList.value = it
                } else {
                    productsList.value = it
                }
            }
        }
    }

    fun loadCategories() = viewModelScope.launch {
        productsRepository.getAllCategories().collect {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    categoriesList.value = it
                } else {
                    categoriesList.value = it
                }
            }
        }
    }


    fun getProductCategory(category: String) = viewModelScope.launch {
        productsRepository.getProductByCategory(category).collect {
            withContext(Dispatchers.IO) {
                if (it is UiState.Success) {
                    productsCategoryList.value = it
                } else {
                    productsCategoryList.value = it
                }
            }
        }
    }

}