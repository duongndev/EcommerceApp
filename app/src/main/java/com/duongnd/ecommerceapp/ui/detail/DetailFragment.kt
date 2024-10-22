package com.duongnd.ecommerceapp.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.text.NumberFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.databinding.FragmentDetailBinding
import com.duongnd.ecommerceapp.ui.MainActivity
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.utils.UiState
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val sessionManager = SessionManager()

    private val detailViewModel: DetailViewModel by activityViewModels()

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    private var TAG = "DetailFragment"
    private var productId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
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
            val quantity = binding.txtQuantityProductDetail.text.toString().toInt()
            progressDialog.start("Đang thêm sản phẩm vào giỏ hàng...")
            addProductToCart(token, AddToCartRequest(productId, quantity))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getProductById(id: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                detailViewModel.getProductById(id)
                detailViewModel.productsList.collect {
                    if (it is UiState.Success) {
                        showLoading(false)
                        val product = it.data
                        product.forEach { productItem ->
                            productId = productItem._id
                            binding.txtTitleProductDetail.text = productItem.product_name
                            binding.txtPriceProductDetail.text = NumberFormat
                                .getCurrencyInstance(Locale("vi", "VN"))
                                .format(productItem.product_price)

                            binding.txtDescriptionProductDetail.text = productItem.product_desc


                            val imgUrl = productItem.imageUrls
                            imgUrl.forEach { url ->
                                Glide.with(requireContext())
                                    .load(url.secure_url)
                                    .listener(object : RequestListener<Drawable> {
                                        override fun onLoadFailed(
                                            e: GlideException?,
                                            model: Any?,
                                            target: Target<Drawable?>,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            binding.progressBarProductDetail.visibility = View.VISIBLE
                                            return false
                                        }

                                        override fun onResourceReady(
                                            resource: Drawable,
                                            model: Any,
                                            target: Target<Drawable?>?,
                                            dataSource: DataSource,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            binding.progressBarProductDetail.visibility = View.GONE
                                            return false
                                        }
                                    })
                                    .into(binding.imgProductDetails)
                            }

                        }

                    }

                    if (it is UiState.Error) {
                        Log.d(TAG, "getProductById: ${it.message}")
                    }

                    if (it is UiState.Loading) {
                        showLoading(true)
                    }

                }
            }
        }, 3000)
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
    }


    private fun ChipGroup.addChip(context: Context, label: String) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
            addView(this)
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.black)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation(true)
        _binding = null
    }

}