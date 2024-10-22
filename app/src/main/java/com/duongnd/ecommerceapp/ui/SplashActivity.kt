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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.duongnd.ecommerceapp.data.repository.AuthRepository
import com.duongnd.ecommerceapp.databinding.ActivitySplashBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.utils.UiState
import com.duongnd.ecommerceapp.view.auth.AuthActivity
import com.duongnd.ecommerceapp.viewmodel.auth.AuthViewModel
import com.duongnd.ecommerceapp.viewmodel.auth.AuthViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Date

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val sessionManager = SessionManager()
    lateinit var authViewModel: AuthViewModel


    private val TAG = "SplashActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager.SessionManager(this)

        authViewModel = ViewModelProvider(
            this, AuthViewModelFactory(
                AuthRepository(
                    AppModule.provideApi()
                )
            )
        )[AuthViewModel::class.java]

//        val token = sessionManager.getToken()!!
//        checkUser(token)


        Handler(Looper.getMainLooper()).postDelayed({
            val token = sessionManager.getToken()
            Log.d(TAG, "onCreate:  token: $token")
            val isValidToken = checkToken(token!!)
//            checkUser(token)
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

    private fun checkUser(token: String) {
        lifecycleScope.launch {
            authViewModel.checkUser(token)
            authViewModel.dataUser.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        Log.d(TAG, "checkUser: loading")
                    }

                    is UiState.Success -> {
                        Log.d(TAG, "checkUser: success")
                        // log ra message
//                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
                    }

                    is UiState.Error -> {
                        Log.d(TAG, "checkUser: error ${state.message}")
                        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
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