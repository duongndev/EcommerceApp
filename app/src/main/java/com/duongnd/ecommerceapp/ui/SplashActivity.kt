package com.duongnd.ecommerceapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.repository.CheckoutRepository
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.view.auth.AuthActivity
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModel
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Date
import kotlin.getValue

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


    private val sessionManager = SessionManager()

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        sessionManager.SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val token = sessionManager.getToken()
            Log.d(TAG, "onCreate:  token: $token")
            val isValidToken = checkToken(token!!)
            if (isValidToken) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }


    private fun checkToken(token: String): Boolean {
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
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}