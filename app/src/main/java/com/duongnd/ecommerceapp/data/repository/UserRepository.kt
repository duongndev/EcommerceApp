package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.user.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {
    fun getUser(token: String, onDataUserListener: onDataUserListener){

    }

    interface onDataUserListener {
        fun onSuccess(user: User)
        fun onFail(error: String)
    }
}