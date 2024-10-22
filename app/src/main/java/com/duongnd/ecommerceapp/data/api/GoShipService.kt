package com.duongnd.ecommerceapp.data.api

import com.duongnd.ecommerceapp.data.model.carrier.Carrier
import com.duongnd.ecommerceapp.data.model.goship.GoShipCity
import com.duongnd.ecommerceapp.data.model.goship.GoShipDistricts
import com.duongnd.ecommerceapp.data.model.goship.GoShipWards
import com.duongnd.ecommerceapp.data.request.goship.GoShipRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GoShipService {

    @GET("/api/v2/cities")
    suspend fun getAllCities(
        @Header("Authorization") token: String
    ): Response<GoShipCity>

    @GET("/api/v2/cities/{code}/districts")
    suspend fun getAllCityDistricts(
        @Header("Authorization") token: String,
        @Path("code") code: String
    ): Response<GoShipDistricts>

    @GET("api/v2/districts/{code}/wards")
    suspend fun getAllDistrictWards(
        @Header("Authorization") token: String,
        @Path("code") code: String,
    ): Response<GoShipWards>

    @POST("/api/v2/rates")
    suspend fun getRates(
        @Header("Authorization") token: String,
        @Body goShipRequest: GoShipRequest
    ): Response<Carrier>
}