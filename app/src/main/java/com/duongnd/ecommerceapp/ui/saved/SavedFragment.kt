package com.duongnd.ecommerceapp.ui.saved

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.adapter.WishlistAdapter
import com.duongnd.ecommerceapp.data.model.wishlist.WishlistItem
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.repository.WishlistRepository
import com.duongnd.ecommerceapp.data.request.WishlistRequest
import com.duongnd.ecommerceapp.databinding.FragmentSavedBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.cart.CartViewModel
import com.duongnd.ecommerceapp.viewmodel.cart.CartViewModelFactory
import com.duongnd.ecommerceapp.viewmodel.wishlist.WishlistViewModel
import com.duongnd.ecommerceapp.viewmodel.wishlist.WishlistViewModelFactory

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private lateinit var wishlistAdapter: WishlistAdapter
    private val wishlistList = mutableListOf<WishlistItem>()
    private val sessionManager = SessionManager()
    private val wishlistViewModel: WishlistViewModel by viewModels {
        WishlistViewModelFactory(WishlistRepository(ecommerceApiService = AppModule.provideApi()))
    }

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    private val TAG = "SavedFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        wishlistAdapter = WishlistAdapter(wishlistList, requireContext())
        binding.recyclerViewProductWishlist.setHasFixedSize(true)
        binding.recyclerViewProductWishlist.adapter = wishlistAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog.start("Loading.....")

        sessionManager.SessionManager(requireContext())
        var token = sessionManager.getToken()!!
        var userId = sessionManager.getUserId()!!

        getWishlist(token, userId)

        wishlistAdapter.clickToSaved = { itemView, product ->
            product.productId.let {
                Toast.makeText(requireContext(), "Đã thêm ${it.name_product} vào đã lưu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWishlist(token: String, userId: String) {
        with(wishlistViewModel) {
            getUserWishlist(token, userId)
            wishlistItems.observe(viewLifecycleOwner) {
                if (it != null) {
                    wishlistList.clear()
                    wishlistList.addAll(it)
                    wishlistAdapter.notifyDataSetChanged()
                }
            }

            loading.observe(viewLifecycleOwner) {
                if (it) {
                    progressDialog.start("Loading")
                } else {
                    progressDialog.stop()
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                Log.d(TAG, "onViewCreated: $it")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}