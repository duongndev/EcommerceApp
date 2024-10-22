package com.duongnd.ecommerceapp.data.model.carrier

data class CarrierData(
    val carrier_logo: String,
    val carrier_name: String,
    val carrier_short_name: String,
    val cod_fee: Int,
    val discount: Int,
    val expected: String,
    val id: String,
    val insurrance_fee: Int,
    val is_apply_only: Boolean,
    val location_fee: Int,
    val location_first_fee: Int,
    val location_step_fee: Int,
    val oil_fee: Int,
    val price_table_id: Int,
    val promotion_id: Int,
    val remote_area_fee: Int,
    val report: Report,
    val return_fee: Int,
    val service: String,
    val service_fee: Int,
    val total_amount: Int,
    val total_amount_carrier: Int,
    val total_amount_shop: Int,
    val total_fee: Int,
    val weight_fee: Int
)