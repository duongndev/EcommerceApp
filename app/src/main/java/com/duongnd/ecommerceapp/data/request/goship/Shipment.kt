package com.duongnd.ecommerceapp.data.request.goship

data class Shipment(
    val address_from: AddressFrom,
    val address_to: AddressTo,
    val parcel: Parcel
)