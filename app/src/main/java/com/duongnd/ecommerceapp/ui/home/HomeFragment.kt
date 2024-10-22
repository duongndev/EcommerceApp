package com.duongnd.ecommerceapp.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.adapter.HomeAdapter
import com.duongnd.ecommerceapp.data.model.product.ProductItem
import com.duongnd.ecommerceapp.databinding.FragmentHomeBinding
import com.duongnd.ecommerceapp.ui.bottomsheet.FilterBottomSheetFragment
import com.duongnd.ecommerceapp.ui.detail.DetailFragment
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.utils.UiState
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
    private var productList = ArrayList<ProductItem>()
    private var isImageHeartSelected = false

    private val viewModel: NewHomeViewModel by viewModels()

    private val TAG = "HomeFragment"
    private val sessionManager = SessionManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeAdapter = HomeAdapter(productList, requireContext())

        binding.recyclerViewProduct.setHasFixedSize(true)
        binding.recyclerViewProduct.adapter = homeAdapter

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        binding.chipGroup.addChip(requireContext(), "All")
        getAllCategory()
        val firstChip: Chip = binding.chipGroup.getChildAt(0) as Chip
        firstChip.isChecked = true

        binding.recyclerViewProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })

        if (firstChip.isChecked) {
            productList.clear()
            getAllProducts()
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            for (id in checkedIds) {
                val chip = group.findViewById<Chip>(id)
                if (chip.id == firstChip.id) {
                    showLoading(true)
                    productList.clear()
                    getAllProducts()
                } else {
                    showLoading(true)
                    val name = chip.text.toString()
                    productList.clear()
                    getProductsByCategory(name)
                }
            }
        }

        adapterListener()

        binding.btnFitter.setOnClickListener {
            val filterBottomSheetFragment = FilterBottomSheetFragment()
            filterBottomSheetFragment.show(
                parentFragmentManager,
                FilterBottomSheetFragment::class.java.simpleName
            )
        }


    }

    private fun adapterListener() {
        homeAdapter.clickToSaved = { click, product ->
            val imageButton = click as ImageButton
            if (isImageHeartSelected) {
                click.isSelected = false
                Toast.makeText(context, "Đã xóa khỏi mục yêu thích", Toast.LENGTH_SHORT)
                    .show()
                imageButton.setImageResource(R.drawable.ic_heart)
            } else {
                click.isSelected = true
                Toast.makeText(context, "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT)
                    .show()
                imageButton.setImageResource(R.drawable.ic_heart_filled)
                // print product name
                Log.d(TAG, "onViewCreated: ${product.product_name}")
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

    }

    private fun getAllCategory() {
        lifecycleScope.launch {
            viewModel.loadCategories()
            viewModel.categoriesList.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        showLoading(true)
                    }

                    is UiState.Success -> {
                        showLoading(false)
                        val categories = uiState.data
                        for (category in categories) {
                            binding.chipGroup.addChip(requireContext(), category)
                        }
                    }

                    is UiState.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
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

    private fun getProductsByCategory(category: String) {
        lifecycleScope.launch {
            viewModel.getProductCategory(category)
            viewModel.productsCategoryList.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        showLoading(true)
                    }

                    is UiState.Success -> {
                        showLoading(false)
                        productList.clear()
                        productList.addAll(uiState.data)
                        homeAdapter.notifyDataSetChanged()
                    }

                    is UiState.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getAllProducts() {
        lifecycleScope.launch {
            viewModel.loadProductsList()
            viewModel.productsList.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        showLoading(true)
                    }

                    is UiState.Success -> {
                        showLoading(false)
                        productList.clear()
                        productList.addAll(uiState.data)
                        homeAdapter.notifyDataSetChanged()
                    }

                    is UiState.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: destroy")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: destroy")
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: stop")
    }

}