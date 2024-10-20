package com.duongnd.ecommerceapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SessionManager {
    private val PREF_NAME = "UserSession"
    private val KEY_USER_ID = "user_id"
    private val KEY_IS_LOGGED_IN = "is_logged_in"
    private val KEY_TOKEN = ""
    private val KEY_ADDRESS_ID = ""
    private val KEY_SAVED_PRODUCTS = "produced_id"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("NotConstructor")
    fun SessionManager(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setToken(token: String) {
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, "")
    }

    fun setUserId(userId: String) {
        editor.putString(KEY_USER_ID, userId)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, "")
    }


    fun setAddressId(userId: String, addressId: String) {
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_ADDRESS_ID, addressId)
        editor.apply()
    }

    fun getAddressId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, "")
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }


    fun setSavedProducts(productId: String) {
        editor.putString(KEY_SAVED_PRODUCTS, productId)
        editor.apply()
    }

    fun clearSavedProducts() {
        editor.putString(KEY_SAVED_PRODUCTS, "")
        editor.apply()
    }

    fun getSavedProducts(): String? {
        return sharedPreferences.getString(KEY_SAVED_PRODUCTS, "")
    }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }

}