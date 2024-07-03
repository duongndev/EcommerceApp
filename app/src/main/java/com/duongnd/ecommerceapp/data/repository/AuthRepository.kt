package com.duongnd.ecommerceapp.data.repository

import com.duongnd.ecommerceapp.data.api.ApiService
import com.duongnd.ecommerceapp.data.model.login.DataLogin
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    fun getLogin(loginRequest: LoginRequest, onDataLoginListener: onDataLoginListener) {

        apiService.login(loginRequest).enqueue(object : Callback<DataLogin> {
            override fun onResponse(
                call: Call<DataLogin>,
                response: Response<DataLogin>
            ) {
                if (response.isSuccessful) {
                    onDataLoginListener.onSuccess(response.body()!!)
                } else {
                    onDataLoginListener.onFail(response.errorBody()!!.string())
                }
            }

            override fun onFailure(p0: Call<DataLogin>, p1: Throwable) {
                onDataLoginListener.onFail(p1.message!!)
            }

        })
    }

    fun getUser(token: String, onDataUserListener: onDataUserListener) {
        CoroutineScope(Dispatchers.IO).launch {
            apiService.getUser(token).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        onDataUserListener.onSuccess(response.body()!!)
                    } else {
                        onDataUserListener.onFail(response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    onDataUserListener.onFail(t.message!!)
                }

            })
        }
    }

    interface onDataLoginListener {
        fun onSuccess(dataLogin: DataLogin)
        fun onFail(error: String)
    }

    interface onDataUserListener {
        fun onSuccess(user: User)
        fun onFail(error: String)
    }
}