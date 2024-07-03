package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.ProductDetail
import com.duongnd.ecommerceapp.data.model.product.Products
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRepository(private val apiService: ApiService) {

    fun getAllProducts(onDataProductsListener: onDataProductsListener) {
        apiService.getAllProducts().enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    onDataProductsListener.onDataSuccess(response.body()!!.data)
                } else {
                    onDataProductsListener.onFail(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                onDataProductsListener.onFail(t.message!!)
            }
        })
    }

    fun getProductsById(id: String, onDataProductDetailListener: onDataProductDetailListener) {
        apiService.getProductsById(id).enqueue(object : Callback<ProductDetail> {
            override fun onResponse(call: Call<ProductDetail>, response: Response<ProductDetail>) {
                if (response.isSuccessful) {
                    onDataProductDetailListener.onDataSuccess(response.body()!!.data)
                } else {
                    onDataProductDetailListener.onDataFail(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<ProductDetail>, t: Throwable) {
                onDataProductDetailListener.onDataFail(t.message!!)
            }

        })

    }



    interface onDataProductsListener {
        fun onDataSuccess(products: ArrayList<DataProduct>)
        fun onFail(error: String)
    }

    interface onDataProductItemListener {
        fun onDataSuccess(dataProduct: ArrayList<DataProduct>)
        fun onFail(error: String)
    }

    interface onDataProductDetailListener {
        fun onDataSuccess(dataProduct: DataProduct)
        fun onDataFail(error: String)
    }

}