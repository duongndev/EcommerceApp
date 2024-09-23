package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiResponse
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.model.wishlist.Wishlist
import com.duongnd.ecommerceapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WishlistRepository @Inject constructor(
    private val ecommerceApiService: EcommerceApiService
) : ApiResponse() {

    suspend fun getUsersWishlist(token: String, userId: String): Flow<Resource<Wishlist>> {
        return flow {
            emit(safeApiCallWishlist {
                ecommerceApiService.getWishlist(token, userId)
            })
        }.flowOn(Dispatchers.IO)
    }


}