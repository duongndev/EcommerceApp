package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.address.Address
import com.duongnd.ecommerceapp.data.model.address.AddressItem
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.order.Order
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.ProductDetail
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRepository(private val apiService: ApiService) {

    fun getAllProducts(onDataProductsListener: onDataProductsListener) {
        apiService.getAllProducts().enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val data = response.body()!!.products
                    onDataProductsListener.onDataSuccess(data)
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

    fun addToCart(
        token: String,
        addToCartRequest: AddToCartRequest,
        onDataCartListener: onDataCartListener
    ) {
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


    fun getAllAddresses(
        token: String,
        id: String,
        onDataAddressItemListener: onDataAddressItemListener
    ) {
        apiService.getAllAddresses(token, id).enqueue(object : Callback<Address> {
            override fun onResponse(p0: Call<Address>, p1: Response<Address>) {
                if (p1.isSuccessful) {
                    val address = p1.body()
                    onDataAddressItemListener.onDataSuccess(address!!)
                } else {
                    onDataAddressItemListener.onFail(p1.errorBody().toString())
                }
            }

            override fun onFailure(p0: Call<Address>, p1: Throwable) {
                onDataAddressItemListener.onFail(p1.message!!)
            }
        })
    }

    fun createOrder(token: String, orderItemRequest: OrderItemRequest, onDataOrderItemListener: onDataOrderItemListener){
        apiService.addOrder(token, orderItemRequest).enqueue(object : Callback<Order>{
            override fun onResponse(p0: Call<Order>, p1: Response<Order>) {
                if (p1.isSuccessful) {
                    val order = p1.body()
                    onDataOrderItemListener.onDataSuccess(order!!)
                } else {
                    onDataOrderItemListener.onDataFail(p1.errorBody().toString())
                }
            }

            override fun onFailure(p0: Call<Order>, p1: Throwable) {
                onDataOrderItemListener.onDataFail(p1.message!!)
            }

        })
    }


    interface onDataProductsListener {
        fun onDataSuccess(products: List<DataProduct>)
        fun onFail(error: String)
    }

    interface onDataOrderItemListener {
        fun onDataSuccess(order: Order)
        fun onDataFail(error: String)
    }

    interface onDataAddressItemListener {
        fun onDataSuccess(dataProduct: ArrayList<AddressItem>)
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