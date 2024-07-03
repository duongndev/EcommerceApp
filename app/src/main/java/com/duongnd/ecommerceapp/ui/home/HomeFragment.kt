package com.duongnd.ecommerceapp.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.adapter.HomeAdapter
import com.duongnd.ecommerceapp.data.api.RetrofitClient
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.repository.HomeRepository
import com.duongnd.ecommerceapp.databinding.FragmentHomeBinding
import com.duongnd.ecommerceapp.ui.bottomsheet.FilterBottomSheetFragment
import com.duongnd.ecommerceapp.ui.detail.DetailFragment
import com.duongnd.ecommerceapp.viewmodel.home.HomeViewModel
import com.duongnd.ecommerceapp.viewmodel.home.HomeViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private var productList = ArrayList<DataProduct>()
    private var isImageHeartSelected = false
    private var categoryId: ArrayList<String> = ArrayList()


    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeRepository(apiService = RetrofitClient.apiService))
    }

    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeAdapter = HomeAdapter(productList, requireContext())

        binding.recyclerViewProduct.setHasFixedSize(true)
        binding.recyclerViewProduct.adapter = homeAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        binding.chipGroup.addChip(requireContext(), "All")
        getAllCategories()
        val firstChip: Chip = binding.chipGroup.getChildAt(0) as Chip
        firstChip.isChecked = true

        if (firstChip.isChecked) {
            getAllProducts()
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            for (id in checkedIds) {
                val chip = group.findViewById<Chip>(id)
                if (chip.id == firstChip.id) {
                    showLoading(true)
                    getAllProducts()
                } else {
                    showLoading(true)
                    val name = chip.text.toString()
                    updateDataRecyclerViewByCategory(name)
                }
            }
        }

        homeAdapter.clickToSaved = {
            val imageButton = it as ImageButton
            if (isImageHeartSelected) {
                it.isSelected = false
                Toast.makeText(context, "Đã xóa khỏi mục yêu thích", Toast.LENGTH_SHORT)
                    .show()
                imageButton.setImageResource(R.drawable.ic_heart)
            } else {
                it.isSelected = true
                Toast.makeText(context, "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT)
                    .show()
                imageButton.setImageResource(R.drawable.ic_heart_filled)
            }

            isImageHeartSelected = !isImageHeartSelected
        }

        homeAdapter.clickToDetail = {
            val detailFragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("id", it._id)
            detailFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_main_activity, detailFragment)
                .addToBackStack(detailFragment::class.java.simpleName)
                .commit()

        }

        homeAdapter.clickToDetail = {
            val detailFragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("id", it._id)
            detailFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_main_activity, detailFragment)
                .addToBackStack(detailFragment::class.java.simpleName)
                .commit()
        }

        binding.btnFitter.setOnClickListener {
            val filterBottomSheetFragment = FilterBottomSheetFragment()
            filterBottomSheetFragment.show(
                parentFragmentManager,
                FilterBottomSheetFragment::class.java.simpleName
            )
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

    private fun getAllProducts() {
        Handler(Looper.getMainLooper()).postDelayed({
            homeViewModel.getAllProducts()
            homeViewModel._liveDataProducts.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "onViewCreated: $it")
                productList.clear()
                productList.addAll(it)
                homeAdapter.notifyDataSetChanged()
                showLoading(false)
            })
        }, 2000)
    }


    private fun updateDataRecyclerViewByCategory(name: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            homeViewModel.getProductByCategoryId(name)
            homeViewModel._liveDataProducts.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "onViewCreated: $it")
                productList.clear()
                productList.addAll(it)
                homeAdapter.notifyDataSetChanged()
                showLoading(false)
            })
        }, 2000)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.shimmerFrameLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerViewProduct.visibility = if (isLoading) View.GONE else View.VISIBLE

        if (isLoading) {
            binding.shimmerFrameLayout.startShimmer()
        } else {
            binding.shimmerFrameLayout.stopShimmer()
        }
    }

    private fun getAllCategories() {
        homeViewModel.getAllCategories()
        homeViewModel._liveDataCategories.observe(viewLifecycleOwner, Observer {
            it.forEach { category ->
                binding.chipGroup.addChip(requireContext(), category.nameCate)
            }
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: stop")
    }

}