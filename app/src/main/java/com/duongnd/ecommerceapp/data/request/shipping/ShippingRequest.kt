package com.duongnd.ecommerceapp.data.request.shipping

data class ShippingRequest(
    val addressTo: AddressToShip,
    val parcel: ParcelShip
)