package com.duongnd.ecommerceapp.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duongnd.ecommerceapp.adapter.OrderTabAdapter
import com.duongnd.ecommerceapp.databinding.FragmentOrderBinding
import com.google.android.material.tabs.TabLayoutMediator

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OrderTabAdapter(parentFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPagerOrder.adapter = adapter
        TabLayoutMediator(binding.tabLayoutOrder, binding.viewPagerOrder) { tab, position ->
            when (position) {
                0 -> tab.text = "Order History"
                1 -> tab.text = "Order Completed"
            }

        }.attach()
    }

}