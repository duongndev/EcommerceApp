package com.duongnd.ecommerceapp.ui.checkout

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.databinding.ActivityCheckoutBinding
import com.duongnd.ecommerceapp.ui.checkout.fragment.CheckoutHomeFragment

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private var cartItemList = ArrayList<ItemCart>()

    val TAG = "CheckoutActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.let {
            cartItemList = it.getParcelableArrayListExtra<ItemCart>("itemsCart") ?: arrayListOf()
        }

        val checkoutHomeFragment = CheckoutHomeFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("items", cartItemList)
        checkoutHomeFragment.arguments = bundle

        Log.d(TAG, "onCreate: $cartItemList")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_checkout, checkoutHomeFragment)
            .commit()

    }
     fun changeTitle(title: String) {
        binding.txtTitleCheckout.text = title
    }
}