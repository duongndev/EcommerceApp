package com.duongnd.ecommerceapp.data.model.carrier

data class Carrier(
    val code: Int,
    val `data`: List<CarrierData>,
    val status: String
)