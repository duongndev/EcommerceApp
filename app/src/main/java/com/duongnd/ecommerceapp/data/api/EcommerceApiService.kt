package com.duongnd.ecommerceapp.data.api

import com.duongnd.ecommerceapp.data.model.address.Address
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.category.Category
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.model.order.DataOrder
import com.duongnd.ecommerceapp.data.model.order.Order
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.data.model.user.User
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EcommerceApiService {
    // products
    @GET("/products")
    suspend fun getAllProducts(): Response<Products>

    @GET("/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Response<DataProduct>

    @GET("/products/category/{name}")
    suspend fun getProductsByCategoryName(
        @Path("name") name: String
    ): Response<Products>


    // cart
    @GET("/cart/user/{id}")
    suspend fun getUserCart(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<Cart>

    @POST("/cart/")
    suspend fun addCart(
        @Header("Authorization") token: String,
        @Body addToCartRequest: AddToCartRequest
    ): Response<Cart>

    @PUT("/cart/increment/{id}")
    suspend fun incrementQuantityCart(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body cartItemRequest: CartItemRequest
    ): Response<Cart>

    @PUT("/cart/decrement/{id}")
    suspend fun decrementQuantityCart(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body cartItemRequest: CartItemRequest
    ): Response<Cart>


    // login
    @POST("/auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<DataLogin>


    // user
    suspend fun getUser(
        @Header("Authorization")
        token: String
    ): Response<User>

    // category
    @GET("/categories")
    suspend fun getAllCategories(): Response<Category>

    // address
    @GET("/addresses/user/{id}")
    suspend fun getAllAddresses(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Address>

    // order
    @POST("/orders")
    suspend fun addOrder(
        @Header("Authorization") token: String,
        @Body orderItemRequest: OrderItemRequest
    ): Response<DataOrder>


}