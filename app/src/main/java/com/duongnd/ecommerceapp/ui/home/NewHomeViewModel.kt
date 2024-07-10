package com.duongnd.ecommerceapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.ecommerceapp.data.model.category.DataCategory
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.repository.CategoryRepository
import com.duongnd.ecommerceapp.data.repository.ProductsRepository
import com.duongnd.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewHomeViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _productsList: MutableLiveData<ArrayList<DataProduct>> = MutableLiveData()
    val productsList: LiveData<ArrayList<DataProduct>> = _productsList


    private val _categoriesList: MutableLiveData<ArrayList<DataCategory>> = MutableLiveData()
    val categoriesList: LiveData<ArrayList<DataCategory>> = _categoriesList


    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        loadProductsList()
        loadCategoryList()
    }

     fun loadProductsList() = viewModelScope.launch {
        productsRepository.getAllProducts().collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _productsList.postValue(data)
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
    private fun loadCategoryList() = viewModelScope.launch {
        categoryRepository.getAllCategory().collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _categoriesList.postValue(data)
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

    fun loadProductByCategoryName(name: String) = viewModelScope.launch {
        productsRepository.getProductsByCategoryName(name).collect { response ->
            run {
                when (response) {
                    is Resource.Success -> {
                        _loading.value = false
                        val data = response.data!!
                        _productsList.postValue(data)
                        Timber.d("loadProductByCategoryName: $data")
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