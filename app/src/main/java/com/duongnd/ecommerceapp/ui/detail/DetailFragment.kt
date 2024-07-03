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
import androidx.lifecycle.Observer
import com.duongnd.ecommerceapp.data.api.RetrofitClient
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.databinding.FragmentDetailBinding
import com.duongnd.ecommerceapp.ui.MainActivity
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModel
import com.duongnd.ecommerceapp.viewmodel.detail.DetailViewModelFactory

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(MyRepository(apiService = RetrofitClient.apiService))
    }

    private var TAG = "DetailFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showBottomNavigation(false)

        val bundle = arguments
        val id = bundle?.getString("id")
        showLoading(true)
        getProductById(id!!)


        binding.btnAddToCart.setOnClickListener {
            val nameProduct = binding.txtTitleProductDetail.text.toString()
            Toast.makeText(context, nameProduct, Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getProductById(id: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            detailViewModel.geDataProductById(id)
            detailViewModel._liveDataProductDetail.observe(viewLifecycleOwner, Observer {
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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation(true)
    }

}