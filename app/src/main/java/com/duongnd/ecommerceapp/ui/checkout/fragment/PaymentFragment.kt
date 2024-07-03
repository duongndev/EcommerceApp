package com.duongnd.ecommerceapp.ui.checkout.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.databinding.FragmentPaymentBinding
import com.duongnd.ecommerceapp.ui.checkout.CheckoutActivity
import com.duongnd.ecommerceapp.ui.dialog.DialogPaymentFragment

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CheckoutActivity).changeTitle("Payment Method")

        binding.btnAddPayment.setOnClickListener {
            val dialogPaymentFragment = DialogPaymentFragment()
            dialogPaymentFragment.show((activity as CheckoutActivity).supportFragmentManager, "dialog")

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as CheckoutActivity).changeTitle("Checkout")
    }

}