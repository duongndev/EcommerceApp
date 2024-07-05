package com.duongnd.ecommerceapp.data.repository

import android.util.Log
import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.ProductDetail
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
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



    fun addToCart(token: String, addToCartRequest: AddToCartRequest, onDataCartListener: onDataCartListener) {
        apiService.addCart(token, addToCartRequest).enqueue(object : Callback<Cart> {
            override fun onResponse(p0: Call<Cart>, p1: Response<Cart>) {
                if (p1.isSuccessful) {
                    val cart = p1.body()
                    onDataCartListener.onDataSuccess(cart!!)
                } else {
                    onDataCartListener.onDataFail(p1.errorBody().toString())
                }
            }

            override fun onFailure(p0: Call<Cart>, p1: Throwable) {
                onDataCartListener.onDataFail(p1.message!!)
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

    interface onDataCartListener {
        fun onDataSuccess(car: Cart)
        fun onDataFail(error: String)
    }
}