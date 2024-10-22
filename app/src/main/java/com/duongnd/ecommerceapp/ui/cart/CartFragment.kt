package com.duongnd.ecommerceapp.ui.cart

import android.content.Intent
import android.icu.text.NumberFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.duongnd.ecommerceapp.adapter.CartAdapter
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.request.CartItemRequest
import com.duongnd.ecommerceapp.databinding.FragmentCartBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.ui.checkout.CheckoutActivity
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.cart.CartViewModel
import com.duongnd.ecommerceapp.viewmodel.cart.CartViewModelFactory
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter

    private var cartItemList = ArrayList<ItemCart>()
    private val sessionManager = SessionManager()

    private val cartViewModel: CartViewModel by activityViewModels()

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    val TAG = "CartFragment"
    var cartId: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        cartAdapter = CartAdapter(cartItemList, requireContext())

        binding.recyclerViewCart.setHasFixedSize(true)
        binding.recyclerViewCart.adapter = cartAdapter
        sessionManager.SessionManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = sessionManager.getToken()!!

        progressDialog.start("Loading Cart...")
        Handler(Looper.getMainLooper()).postDelayed({
            getCartItemList(token)
        }, 2000)

        cartAdapter.incrementQuantity = {
            val productId = CartItemRequest(it.productId)
            incrementQuantity(cartId, token, productId)
        }
        cartAdapter.decrementQuantity = {
            val productId = it.productId
            decrementQuantity(cartId, token, CartItemRequest(productId))
        }

        cartAdapter.deleteCart = {
            val idCart = it._id
            Toast.makeText(context, "Delete idCart $idCart", Toast.LENGTH_SHORT).show()
        }

        binding.btnCheckoutDetail.setOnClickListener {
            val intent = Intent(requireActivity(), CheckoutActivity::class.java)
            intent.putParcelableArrayListExtra("itemsCart", cartItemList)
            intent.putExtra("totalAmount", binding.txtSubtotalValue.text.toString())
            Log.d(TAG, "onViewCreated: $intent")
            startActivity(intent)
        }

    }

    private fun getCartItemList(token: String) {
        with(cartViewModel) {
            getUserCart(token)
            cartItems.observe(viewLifecycleOwner) {
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


                    val locale = Locale("vi", "VN")
                    val numberFormat = NumberFormat.getInstance(locale)
                    val formattedPrice = numberFormat.format(it.totalAmount)

                    binding.txtSubtotalValue.text = "$formattedPrice vnđ"
                    cartAdapter.notifyDataSetChanged()
                    progressDialog.stop()
                }
            }
            errorMessage.observe(viewLifecycleOwner) {
                Log.d(TAG, "observeDataError: $it")
                progressDialog.stop()
                binding.layoutCartEmpty.visibility = View.VISIBLE
            }
            loading.observe(viewLifecycleOwner) {
                progressDialog.stop()
                if (it) progressDialog.start("Loading Cart...")
            }
        }
    }

    private fun incrementQuantity(id: String, token: String, cartItemRequest: CartItemRequest) {
        with(cartViewModel) {
            incrementQuantityCart(id, token, cartItemRequest)
            cartItems.observe(viewLifecycleOwner) {
                Log.d(TAG, "onIncrementQuantity: $it")
                if (it.itemsCart.isEmpty()) {
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
                    val locale = Locale("vi", "VN")
                    val numberFormat = NumberFormat.getInstance(locale)
                    val formattedPrice = numberFormat.format(it.totalAmount)
                    binding.txtSubtotalValue.text = "$formattedPrice vnđ"
                    cartAdapter.notifyDataSetChanged()
                }
            }
            errorMessage.observe(viewLifecycleOwner) {
                Log.d(TAG, "observeIncrementQuantityError: $it")
            }
            loading.observe(viewLifecycleOwner) {
                if (it) progressDialog.start("Loading...")
            }
        }
    }

    private fun decrementQuantity(id: String, token: String, cartItemRequest: CartItemRequest) {
        with(cartViewModel) {
            decrementQuantityCart(id, token, cartItemRequest)
            cartItems.observe(viewLifecycleOwner) {
                Log.d(TAG, "onIncrementQuantity: $it")
                if (it.itemsCart.isEmpty()) {
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
                    val locale = Locale("vi", "VN")
                    val numberFormat = NumberFormat.getInstance(locale)
                    val formattedPrice = numberFormat.format(it.totalAmount)
                    binding.txtSubtotalValue.text = "$formattedPrice vnđ"
                    cartAdapter.notifyDataSetChanged()
                }
            }
            errorMessage.observe(viewLifecycleOwner) {
                Log.d(TAG, "observeIncrementQuantityError: $it")
            }
            loading.observe(viewLifecycleOwner) {
                if (it) progressDialog.start("Loading...")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: destroy")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: destroy")
    }

}