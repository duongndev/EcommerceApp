package com.duongnd.ecommerceapp.data.api

import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.category.Category
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.model.product.ProductDetail
import com.duongnd.ecommerceapp.data.model.product.Products
import com.duongnd.ecommerceapp.data.model.user.User
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // products
    @GET("/products")
    fun getAllProducts(): Call<Products>

    @GET("/products/{id}")
    fun getProductsById(
        @Path("id") id: String
    ): Call<ProductDetail>

    @GET("/products/category/{name}")
    fun getProductsByCategoryId(
        @Path("name") name: String
    ): Call<Products>

    //Cart
    @GET("/cart/user/{id}")
    fun getUserCart(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<Cart>

    @POST("/cart/")
    fun addCart(
        @Header("Authorization") token: String,
        @Body addToCartRequest: AddToCartRequest
    ) : Call<Cart>

    @PUT("/cart/increment/{id}")
    fun incrementQuantity(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body cartItemRequest: CartItemRequest
    ) : Call<Cart>

    @PUT("/cart/decrement/{id}")
    fun decrementQuantity(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body cartItemRequest: CartItemRequest
    ) : Call<Cart>

    //login
    @POST("auth/login")
    fun login(
        @Body login: LoginRequest
    ): Call<DataLogin>

    //user
    @GET("users/me")
    fun getUser(
        @Header("Authorization")
        token: String
    ): Call<User>


    // categories
    @GET("/categories")
    fun getAllCategories(): Call<Category>

}