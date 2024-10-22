package com.duongnd.ecommerceapp.data.api

import com.duongnd.ecommerceapp.data.model.carrier.Carrier
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.model.order.DataOrder
import com.duongnd.ecommerceapp.data.model.product.Category
import com.duongnd.ecommerceapp.data.model.product.Product
import com.duongnd.ecommerceapp.data.model.product.ProductItem
import com.duongnd.ecommerceapp.data.model.user.UsersItem
import com.duongnd.ecommerceapp.data.model.user.address.Address
import com.duongnd.ecommerceapp.data.model.wishlist.Wishlist
import com.duongnd.ecommerceapp.data.model.wishlist.WishlistItem
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.data.request.AddToWishlistRequest
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.data.request.goship.GoShipRequest
import com.duongnd.ecommerceapp.data.request.shipping.ShippingRequest
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
    suspend fun getAllProducts(): Response<Product>

    @GET("/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Response<ProductItem>

    @GET("/products/categories")
    suspend fun getAllCategories(): Response<Category>

    @GET("/products/category/{name}")
    suspend fun getProductsByCategoryName(
        @Path("name") name: String
    ): Response<Product>


    // cart
    @GET("/cart/user")
    suspend fun getUserCart(
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

    // check user
    @GET("/auth/checkUser")
    suspend fun checkUser(
        @Header("Authorization") token: String
    ): Response<UsersItem>


    // user
    @GET("/users/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
    ): Response<UsersItem>

    // address
    @GET("/users/address")
    suspend fun getAllAddresses(
        @Header("Authorization") token: String,
    ): Response<Address>

    // order
    @POST("/orders")
    suspend fun addOrder(
        @Header("Authorization") token: String,
        @Body orderItemRequest: OrderItemRequest
    ): Response<DataOrder>


    // wishlist
    suspend fun addWishlist(
        @Header("Authorization") token: String,
        @Body wishlistRequest: AddToWishlistRequest
    ): Response<WishlistItem>

    @GET("/wishlist/{userId}")
    suspend fun getWishlist(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<Wishlist>


    @POST("/orders/shipping-fee")
    suspend fun getShippingFee(
        @Body shippingRequest: ShippingRequest
    ): Response<Carrier>
}