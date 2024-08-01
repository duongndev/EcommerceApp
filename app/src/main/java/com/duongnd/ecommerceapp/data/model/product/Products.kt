package com.duongnd.ecommerceapp.data.model.product

data class Products(
    val currentPage: Int,
    val totalItems: Int,
    val totalPages: Int,
    val products: List<DataProduct> = ArrayList() //
)