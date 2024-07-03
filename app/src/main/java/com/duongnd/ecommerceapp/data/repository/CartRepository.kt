package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRepository(private val apiService: ApiService) {

    fun getUserCart(id: String, token: String, onDataCartListener: onDataCartListener) {
        apiService.getUserCart(id, token).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    val cart = response.body()
                    onDataCartListener.onDataSuccess(cart!!)
                } else {
                    onDataCartListener.onDataFail(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                onDataCartListener.onDataFail(t.message!!)
            }

        })
    }

    fun incrementQuantity(
        id: String,
        token: String,
        cartItemRequest: CartItemRequest,
        onDataCartListener: onDataCartListener
    ) {
        apiService.incrementQuantity(id, token, cartItemRequest).enqueue(object : Callback<Cart> {
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

    fun decrementQuantity(
        id: String,
        token: String,
        cartItemRequest: CartItemRequest,
        onDataCartListener: onDataCartListener
    ) {
        apiService.decrementQuantity(id, token, cartItemRequest).enqueue(object : Callback<Cart> {
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

    interface onDataCartListener {
        fun onDataSuccess(car: Cart)
        fun onDataFail(error: String)
    }
}