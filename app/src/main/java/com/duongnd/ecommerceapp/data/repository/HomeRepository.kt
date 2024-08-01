package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.category.Category
import com.duongnd.ecommerceapp.data.model.category.DataCategory
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.Products
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository(private val apiService: ApiService) {

    fun getAllProducts(onDataProductsListener: onDataProductsListener) {
        apiService.getAllProducts().enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val data = response.body()!!.products
                    onDataProductsListener.onDataSuccess(data)
                } else {
                    onDataProductsListener.onDataFail(response.message())
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                onDataProductsListener.onDataFail(t.message!!)
            }
        })
    }



    fun getAllCategories(onDataCategoriesListener: onDataCategoriesListener) {
        apiService.getAllCategories().enqueue(object: Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    onDataCategoriesListener.onDataSuccess(response.body()!!)
                } else {
                    onDataCategoriesListener.onDataFail(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                onDataCategoriesListener.onDataFail(t.message!!)
            }
        })

    }

    fun getProductByCategoryId(name: String, onDataProductsListener: onDataProductsListener){
        apiService.getProductsByCategoryId(name).enqueue(object: Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val data = response.body()!!.products
                    onDataProductsListener.onDataSuccess(data)
                } else {
                    onDataProductsListener.onDataFail(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                onDataProductsListener.onDataFail(t.message!!)
            }
        })
    }


    interface onDataCategoriesListener{
        fun onDataSuccess(dataCategories: ArrayList<DataCategory>)
        fun onDataFail(error: String)
    }

    interface onDataProductsListener {
        fun onDataSuccess(dataProducts: List<DataProduct>)
        fun onDataFail(error: String)
    }

}