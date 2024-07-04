package com.duongnd.ecommerceapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.databinding.ActivityMainBinding
import com.duongnd.ecommerceapp.ui.account.AccountFragment
import com.duongnd.ecommerceapp.ui.cart.CartFragment
import com.duongnd.ecommerceapp.ui.home.HomeFragment
import com.duongnd.ecommerceapp.ui.saved.SavedFragment
import com.duongnd.ecommerceapp.view.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNavMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_cart -> replaceFragment(CartFragment())
                R.id.nav_profile -> replaceFragment(AccountFragment())
                R.id.nav_saved -> replaceFragment(SavedFragment())
                R.id.nav_search -> replaceFragment(SearchFragment())
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view_main_activity, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

    fun showBottomNavigation(show: Boolean) {
        if (show) {
            binding.bottomNavMain.visibility = View.VISIBLE
        } else {
            binding.bottomNavMain.visibility = View.GONE
        }
    }
}