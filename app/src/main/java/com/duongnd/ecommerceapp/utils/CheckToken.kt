package com.duongnd.ecommerceapp.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.util.Date

fun checkToken(token: String, context: Context): Boolean {
    val sessionManager = SessionManager()
    sessionManager.SessionManager(context)
    val TAG = "TokenChecker"

    if (token.isBlank()) {
        return false
    }
    try {
        // Token JWT có 3 phần tách biệt bởi dấu chấm: header, payload, signature
        val parts = token.split(".")
        if (parts.size != 3) {
            return false
        }
        // Phần payload chứa thông tin về ngày hết hạn (exp)
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val payloadJson = JSONObject(payload)

        // Lấy giá trị của exp (thời gian hết hạn) từ payload
        if (payloadJson.has("exp")) {
            val exp = payloadJson.getLong("exp")
            // So sánh thời gian hết hạn với thời gian hiện tại
            val now = Date().time / 1000
            return now < exp
        }
        // lấy giá trị của id (user id) trong payload
        if (payloadJson.has("id")) {
            val userId = payloadJson.getString("id")
            Log.d(TAG, "checkToken - userId: $userId")
            sessionManager.setUserId(userId)
            return true
        }
        return false
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}