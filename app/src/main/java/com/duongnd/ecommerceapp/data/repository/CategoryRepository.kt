package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.category.Category
import com.duongnd.ecommerceapp.data.model.category.DataCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepository(private val apiService: ApiService) {

//    fun getAllCategories(onDataCategoryListener: onDataCategoryListener){
//        CoroutineScope(Dispatchers.IO).launch{
//            val response = apiService.getAllCategories()
//            if (response.isSuccessful){
//                onDataCategoryListener.onDataSuccess(response.body()!!.data)
//            }else{
//                onDataCategoryListener.onDataFail(response.message())
//            }
//        }
//    }


    interface onDataCategoryListener{
        fun onDataSuccess(dataCategory: List<DataCategory>)
        fun onDataFail(error: String)
    }

}