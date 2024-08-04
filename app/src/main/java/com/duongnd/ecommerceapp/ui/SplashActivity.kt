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
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.utils.checkToken
import com.duongnd.ecommerceapp.view.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Date

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
            val userId = sessionManager.getUserId()
            Log.d(TAG, "onCreate:  token: $token")
            Log.d(TAG, "onCreate:  userId: $userId")
            val isValidToken = checkToken(token!!, this)
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


}