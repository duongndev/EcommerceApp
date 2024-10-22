package com.duongnd.ecommerceapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.databinding.FragmentAccountBinding
import com.duongnd.ecommerceapp.ui.checkout.fragment.AddressFragment
import com.duongnd.ecommerceapp.ui.order.OrderFragment
import com.google.android.material.tabs.TabLayoutMediator

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutOrder.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
               .replace(R.id.fragment_container_view_main_activity, OrderFragment())
               .addToBackStack(OrderFragment::class.java.simpleName)
               .commit()
        }

        binding.layoutAddress.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_main_activity, AddressFragment())
                .addToBackStack(AddressFragment::class.java.simpleName)
                .commit()
        }
    }

}