package com.duongnd.ecommerceapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duongnd.ecommerceapp.ui.account.orderHistory.OrderCompletedFragment
import com.duongnd.ecommerceapp.ui.order.OrderHistoryFragment

class OrderTabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
       return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderHistoryFragment()
            1 -> OrderCompletedFragment()
            else -> OrderHistoryFragment()
        }
    }
}