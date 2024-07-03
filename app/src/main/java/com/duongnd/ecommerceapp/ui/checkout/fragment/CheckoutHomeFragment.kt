package com.duongnd.ecommerceapp.ui.checkout.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.adapter.CheckoutAdapter
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.databinding.FragmentCheckoutHomeBinding
import com.duongnd.ecommerceapp.ui.checkout.CheckoutActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.time.times

class CheckoutHomeFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutHomeBinding
    private var cartItemList = ArrayList<ItemCart>()
    private lateinit var checkoutAdapter: CheckoutAdapter

    val TAG = "CheckoutHomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutHomeBinding.inflate(inflater, container, false)
        arguments.let {
            cartItemList = (it!!.getParcelableArrayList("items") ?: arrayListOf())
        }

       val total = cartItemList.sumOf {
           it.price * it.quantity
       }

        val formatPrice = total.toString().replace("\\D+".toRegex(), "")
            .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")

        binding.txtTotalPriceOrder.text = "$formatPrice vnđ"


        Log.d(TAG, "onCreateView: $cartItemList")
        cartItemList = ArrayList(cartItemList)

        checkoutAdapter = CheckoutAdapter(cartItemList, requireContext())

        binding.recyclerViewItemsCheckout.setHasFixedSize(true)
        binding.recyclerViewItemsCheckout.adapter = checkoutAdapter
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipGroupPayment.addChip(requireContext(), "Thẻ tín dụng", R.drawable.ic_card)
        binding.chipGroupPayment.addChip(
            requireContext(),
            "Thanh toán khi nhận hàng",
            R.drawable.cash
        )
        binding.chipGroupPayment.addChip(requireContext(), "ZaloPay", R.drawable.zalo_pay_logo1)
        binding.chipGroupPayment.addChip(requireContext(), "MoMo", R.drawable.ic_logo_momo)

        val firstChip: Chip = binding.chipGroupPayment.getChildAt(0) as Chip
        firstChip.isChecked = true

        if (firstChip.isChecked) {
            binding.listViewItemsCheckoutPayment.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Thẻ tín dụng", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "1234567812345678"
        )

        val maskCardNumber = users.map {
            maskCardNumber(it)
        }

        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, maskCardNumber)
        binding.listViewItemsCheckoutPayment.adapter = arrayAdapter
        binding.chipGroupPayment.setOnCheckedStateChangeListener { group, checkedIds ->
            for (id in checkedIds) {
                val chip = group.findViewById<Chip>(id)
                if (chip.id == firstChip.id) {
                    binding.listViewItemsCheckoutPayment.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Thẻ tín dụng", Toast.LENGTH_SHORT).show()
                } else {
                    binding.listViewItemsCheckoutPayment.visibility = View.GONE
                    Toast.makeText(requireContext(), chip.text, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.listViewItemsCheckoutPayment.setOnItemClickListener { _, _, position, _ ->
//            val showFullPayment = formatCardNumber(users[position])
//            AlertDialog.Builder(requireContext())
//                .setTitle("Full Card Number")
//                .setMessage(showFullPayment)
//                .setPositiveButton(android.R.string.ok, null)
//                .show()

            val paymentFragment = PaymentFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_checkout, paymentFragment)
                .addToBackStack(null)
                .commit()

        }


        binding.bttPlaceOrder.setOnClickListener {

        }
    }

    private fun maskCardNumber(cardNumber: String): String {
        val maskedNumber = "*".repeat(cardNumber.length - 4) + cardNumber.takeLast(4)
        return maskedNumber.chunked(4).joinToString(" ")
    }
    private fun formatCardNumber(cardNumber: String): String {
        return cardNumber.chunked(4).joinToString(" ")
    }

    private fun ChipGroup.addChip(context: Context, label: String, drawable: Int) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = true
            isChipIconVisible = true
            isCheckedIconVisible = false
            chipIcon = ContextCompat.getDrawable(context, drawable)
            addView(this)
            chipIconTint = ContextCompat.getColorStateList(context, R.color.black)
            chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.black)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    chipIconTint = ContextCompat.getColorStateList(context, R.color.white)
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
                    chipIconTint = ContextCompat.getColorStateList(context, R.color.black)
                }
            }
        }

    }

}