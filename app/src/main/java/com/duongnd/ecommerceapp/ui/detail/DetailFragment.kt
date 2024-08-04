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
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.product.Review
import com.duongnd.ecommerceapp.data.repository.ProductsRepository
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.databinding.FragmentDetailBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.ui.MainActivity
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModel
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val sessionManager = SessionManager()

    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(ProductsRepository(ecommerceApiService = AppModule.provideApi()))
    }

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    private var TAG = "DetailFragment"
    private var productId: String = ""
    private var reviewsList = ArrayList<Review>()

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
            val userId = sessionManager.getUserId()!!
            val quantity = binding.txtQuantityProductDetail.text.toString().toInt()
            progressDialog.start("Đang thêm sản phẩm vào giỏ hàng...")
            addProductToCart(token, AddToCartRequest(userId, productId, quantity))
        }

        binding.txtRatingsProductDetail.setOnClickListener {
            val bundleReview = Bundle()
            // put list review to fragment review
            bundleReview.putParcelableArrayList("reviews", reviewsList)
            val reviewFragment = ReviewFragment()
//            reviewFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_main_activity, reviewFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getProductById(id: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            with(detailViewModel) {
                detailViewModel.getProductById(id)
                productsDetailList.observe(viewLifecycleOwner) {
                    Log.d(TAG, "getProductById: $it")
                    productId = it._id
                    binding.txtTitleProductDetail.text = it.name_product
                    val locale = Locale("vi", "VN")
                    val numberFormat = NumberFormat.getInstance(locale)
                    val formattedPrice = numberFormat.format(it.price)
                    binding.txtPriceProductDetail.text = "$formattedPrice đ"
                    binding.txtDescriptionProductDetail.text = it.description
                    it.size.forEach { size ->
                        binding.chipGroupSizeProductDetail.removeAllViews()
                        binding.chipGroupSizeProductDetail.addChip(
                            requireContext(),
                            size.toString()
                        )
                    }

                    reviewsList.clear()
                    reviewsList.addAll(it.reviews)

                    it.imageUrls.forEach { image ->
                        Glide.with(requireContext())
                            .load(image.secure_url)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.progressBarProductDetail.visibility = View.VISIBLE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    model: Any,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.progressBarProductDetail.visibility = View.GONE
                                    return false
                                }

                            })
                            .into(binding.imgProductDetails)

                    }

                    showLoading(false)

                }
                errorMessage.observe(viewLifecycleOwner) {
                    showLoading(false)
                    progressDialog.stop()
                    Timber.tag(TAG).d("getProductById: " + it)
                }
                loading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }
            }
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
        with(detailViewModel) {
            addToCart(token, addToCartRequest)
            cartItem.observe(viewLifecycleOwner) {
                Timber.tag(TAG).d("addToCart: " + it)
                Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                progressDialog.stop()
            }
            errorMessage.observe(viewLifecycleOwner) {
                progressDialog.stop()
                Timber.tag(TAG).d("addToCart: " + it)
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            loading.observe(viewLifecycleOwner) {
                if (it) progressDialog.start("Loading....")
            }
        }
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