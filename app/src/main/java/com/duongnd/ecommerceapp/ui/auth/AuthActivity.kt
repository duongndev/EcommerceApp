package com.duongnd.ecommerceapp.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.ui.auth.login.LoginFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_auth_activity, LoginFragment())
            .addToBackStack(LoginFragment::class.java.simpleName)
            .commit()

    }
}