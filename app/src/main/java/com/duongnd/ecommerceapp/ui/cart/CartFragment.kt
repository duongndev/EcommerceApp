package com.duongnd.ecommerceapp.ui.cart

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.duongnd.ecommerceapp.adapter.CartAdapter
import com.duongnd.ecommerceapp.data.api.RetrofitClient
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import com.duongnd.ecommerceapp.databinding.FragmentCartBinding
import com.duongnd.ecommerceapp.ui.checkout.CheckoutActivity
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.cart.CartViewModel
import com.duongnd.ecommerceapp.viewmodel.cart.CartViewModelFactory

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter

    private var cartItemList = ArrayList<ItemCart>()
    private val sessionManager = SessionManager()

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory(CartRepository(apiService = RetrofitClient.apiService))
    }

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    val TAG = "CartFragment"
    var cartId: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        cartAdapter = CartAdapter(cartItemList, requireContext())

        binding.recyclerViewCart.setHasFixedSize(true)
        binding.recyclerViewCart.adapter = cartAdapter
        sessionManager.SessionManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = sessionManager.getUserId()!!
        val token = sessionManager.getToken()!!

        progressDialog.start("Loading Cart...")


        Handler(Looper.getMainLooper()).postDelayed({
            getCartItemList(userId, token)
        }, 2000)

        cartAdapter.incrementQuantity = {
            val productId = CartItemRequest(it.productId)
            incrementQuantity(cartId, token, productId)
            cartAdapter.notifyDataSetChanged()
        }
        cartAdapter.decrementQuantity = {
            val productId = it.productId
            decrementQuantity(cartId, token, CartItemRequest(productId))
            cartAdapter.notifyDataSetChanged()
        }

        cartAdapter.deleteCart = {
            val idCart = it._id
            Toast.makeText(context, "Delete idCart $idCart", Toast.LENGTH_SHORT).show()
        }

        binding.btnCheckoutDetail.setOnClickListener {
//            startActivity(Intent(requireActivity(), CheckoutActivity::class.java))

            val intent = Intent(requireActivity(), CheckoutActivity::class.java)
            intent.putParcelableArrayListExtra("itemsCart", cartItemList)
            intent.putExtra("totalAmount", binding.txtSubtotalValue.text.toString())
            Log.d(TAG, "onViewCreated: $intent")
            startActivity(intent)
        }

    }

    private fun getCartItemList(userId: String, token: String) {
        cartViewModel.getUserCart(userId, token)
        cartViewModel._liveDataCart.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            if (it.itemsCart.isEmpty()) {
                progressDialog.stop()
                binding.layoutCartEmpty.visibility = View.VISIBLE
                binding.nestedScrollViewCart.visibility = View.GONE
                binding.layoutTotalCart.visibility = View.GONE
                binding.btnCheckoutDetail.visibility = View.GONE
            } else {
                binding.layoutCartEmpty.visibility = View.GONE
                binding.nestedScrollViewCart.visibility = View.VISIBLE
                binding.layoutTotalCart.visibility = View.VISIBLE
                binding.btnCheckoutDetail.visibility = View.VISIBLE
                cartId = it._id
                cartItemList.clear()
                cartItemList.addAll(it.itemsCart)
                val formatPrice = it.totalAmount.toString().replace("\\D+".toRegex(), "")
                    .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")
                binding.txtSubtotalValue.text = "$formatPrice vnđ"
                cartAdapter.notifyDataSetChanged()
                progressDialog.stop()
            }
        })
        cartViewModel.error.observe(viewLifecycleOwner, Observer {
            progressDialog.stop()
            binding.layoutCartEmpty.visibility = View.VISIBLE
        })
    }

    private fun incrementQuantity(id: String, token: String, cartItemRequest: CartItemRequest) {
        cartViewModel.incrementQuantity(id, token, cartItemRequest)
        cartViewModel._liveDataCartIncrement.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "incrementQuantity: $it")
            cartItemList.clear()
            cartItemList.addAll(it.itemsCart)
            val formatPrice = it.totalAmount.toString().replace("\\D+".toRegex(), "")
                .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")
            binding.txtSubtotalValue.text = "$formatPrice vnđ"
            cartAdapter.notifyDataSetChanged()
        })
    }

    private fun decrementQuantity(id: String, token: String, cartItemRequest: CartItemRequest) {
        cartViewModel.decrementQuantity(id, token, cartItemRequest)
        cartViewModel._dataCartDecrement.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "decrementQuantity: $it")
            cartItemList.clear()
            cartItemList.addAll(it.itemsCart)
            val formatPrice = it.totalAmount.toString().replace("\\D+".toRegex(), "")
                .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")
            binding.txtSubtotalValue.text = "$formatPrice vnđ"
            cartAdapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: destroy")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: destroy")
    }

}