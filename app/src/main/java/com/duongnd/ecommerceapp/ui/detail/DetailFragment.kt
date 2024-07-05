package com.duongnd.ecommerceapp.ui.detail

import android.annotation.SuppressLint
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
import com.duongnd.ecommerceapp.data.api.RetrofitClient
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.databinding.FragmentDetailBinding
import com.duongnd.ecommerceapp.ui.MainActivity
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModel
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModelFactory

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val sessionManager = SessionManager()

    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(MyRepository(apiService = RetrofitClient.apiService))
    }

    private var TAG = "DetailFragment"
    private var productId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        sessionManager.SessionManager(requireContext())
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showBottomNavigation(false)

        val bundle = arguments
        val id = bundle?.getString("id")
        showLoading(true)
        getProductById(id!!)

        binding.imgMinusProductDetail.setOnClickListener {
            val count = binding.txtQuantityProductDetail.text.toString().toInt()
            if (count > 1) {
                binding.txtQuantityProductDetail.text = (count - 1).toString()
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgPlusProductDetail.setOnClickListener {
            val count = binding.txtQuantityProductDetail.text.toString().toInt()
            binding.txtQuantityProductDetail.text = (count + 1).toString()
        }

        binding.btnAddToCart.setOnClickListener {
            val token = sessionManager.getToken()!!
            val userId = sessionManager.getUserId()!!
            val quantity = binding.txtQuantityProductDetail.text.toString().toInt()
            addProductToCart(token, AddToCartRequest(userId, productId, quantity))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getProductById(id: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            detailViewModel.geDataProductById(id)
            detailViewModel._liveDataProductDetail.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "getProductById: $it")
                productId = it._id
                binding.txtTitleProductDetail.text = it.name_product
                binding.txtPriceProductDetail.text = it.price.toString() + " VND"
                binding.txtDescriptionProductDetail.text = it.description
                showLoading(false)
            })
        }, 2000)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.shimmerFrameLayoutDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.layoutContentDetails.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.layoutAddToCart.visibility = if (isLoading) View.GONE else View.VISIBLE

        if (isLoading) {
            binding.shimmerFrameLayoutDetail.startShimmer()
        } else {
            binding.shimmerFrameLayoutDetail.stopShimmer()
        }
    }

    private fun addProductToCart(token: String, addToCartRequest: AddToCartRequest) {
        detailViewModel.addToCart(token, addToCartRequest)
        detailViewModel._liveDataCart.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation(true)
    }

}