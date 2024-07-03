package com.duongnd.ecommerceapp.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.duongnd.ecommerceapp.data.model.category.DataCategory
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.repository.HomeRepository
import com.duongnd.ecommerceapp.utils.SingleLiveEvent

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    val _dataProducts = SingleLiveEvent<ArrayList<DataProduct>>()
    val _liveDataProducts: LiveData<ArrayList<DataProduct>>
        get() = _dataProducts

    val _dataCategories = SingleLiveEvent<ArrayList<DataCategory>>()
    val _liveDataCategories: LiveData<ArrayList<DataCategory>>
        get() = _dataCategories

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    fun getAllProducts() {
        homeRepository.getAllProducts(object : HomeRepository.onDataProductsListener {
            override fun onDataSuccess(dataProducts: ArrayList<DataProduct>) {
                _dataProducts.postValue(dataProducts)
            }

            override fun onDataFail(error: String) {
                _error.postValue(error)
            }

        })
    }

    fun getAllCategories(){
        homeRepository.getAllCategories(object: HomeRepository.onDataCategoriesListener {
            override fun onDataSuccess(dataCategories: ArrayList<DataCategory>) {
                _dataCategories.postValue(dataCategories)
            }

            override fun onDataFail(error: String) {
                _error.postValue(error)
            }

        })
    }

    fun getProductByCategoryId(name: String){
        homeRepository.getProductByCategoryId(name, object: HomeRepository.onDataProductsListener {
            override fun onDataSuccess(dataProducts: ArrayList<DataProduct>) {
                _dataProducts.postValue(dataProducts)
            }

            override fun onDataFail(error: String) {
                _error.postValue(error)
            }
        })
    }

}