package com.duongnd.ecommerceapp.data.model.carrier

data class Report(
    val avg_time_delivery: Int,
    val avg_time_delivery_format: Int,
    val return_percent: Double,
    val success_percent: Double
)