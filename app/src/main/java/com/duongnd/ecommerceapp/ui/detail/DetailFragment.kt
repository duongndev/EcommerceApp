package com.duongnd.ecommerceapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.duongnd.ecommerceapp.data.repository.CartRepository
import com.duongnd.ecommerceapp.data.repository.ProductsRepository
import com.duongnd.ecommerceapp.data.request.AddToCartRequest
import com.duongnd.ecommerceapp.databinding.FragmentDetailBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.ui.MainActivity
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModel
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModelFactory
import timber.log.Timber

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val sessionManager = SessionManager()

    private val detailViewModel: DetailViewModel by viewModels{
        DetailViewModelFactory(ProductsRepository(ecommerceApiService = AppModule.provideApi()))
    }

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

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
            progressDialog.start("Đang thêm sản phẩm vào giỏ hàng...")
            addProductToCart(token, AddToCartRequest(userId, productId, quantity))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getProductById(id: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            with(detailViewModel) {
                detailViewModel.getProductById(id)
                productsDetailList.observe(viewLifecycleOwner) {
                    productId = it._id
                    binding.txtTitleProductDetail.text = it.name_product
                    binding.txtPriceProductDetail.text = it.price.toString()
                    binding.txtDescriptionProductDetail.text = it.description
                    showLoading(false)

                }
                errorMessage.observe(viewLifecycleOwner) {
                    showLoading(false)
                    progressDialog.stop()
                    Timber.tag(TAG).d("getProductById: " + it)
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation(true)
    }

}