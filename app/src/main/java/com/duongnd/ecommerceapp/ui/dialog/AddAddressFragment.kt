package com.duongnd.ecommerceapp.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import com.duongnd.ecommerceapp.AppApplication
import com.duongnd.ecommerceapp.adapter.ItemAdapter
import com.duongnd.ecommerceapp.adapter.ItemDistricAdapter
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipDistricts
import com.duongnd.ecommerceapp.databinding.FragmentAddAddressBinding
import com.duongnd.ecommerceapp.utils.UiState
import com.duongnd.ecommerceapp.viewmodel.GoShipViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.mutableListOf


class AddAddressFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var goShipViewModel: GoShipViewModel
    private val listCity = mutableListOf<DataGoShipCity>()
    private val listDistric = mutableListOf<DataGoShipDistricts>()
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var itemDistricAdapter: ItemDistricAdapter


    private val TAG = "AddAddressFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        goShipViewModel = (requireActivity().application as AppApplication).goShipViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllCities()

        binding.spnAddAddressCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val city = listCity[position]
                    val cityId = city.id.toString()
                    goShipViewModel.getAllDistricts(cityId)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        getAllCityDistrict()

        binding.imgCloseAddAddress.setOnClickListener {
            dismiss()
        }
    }


    private fun getAllCities() {
        lifecycleScope.launch {
            goShipViewModel.citiesState.collectLatest { state ->
                when (state) {
                    is UiState.Error -> {
                        Log.d(TAG, "getAllCities:  ${state.message}")
                    }

                    UiState.Loading -> {
                        Log.d(TAG, "getAllCities:  Loading")
                    }

                    is UiState.Success -> {
                        listCity.clear()
                        listCity.addAll(state.data)
                        itemAdapter = ItemAdapter(requireContext(), listCity)
                        binding.spnAddAddressCity.adapter = itemAdapter
                        Log.d(TAG, "getAllCities:  ${state.data}")
                    }
                }
            }
        }
    }


    private fun getAllCityDistrict() {
        lifecycleScope.launch {
            goShipViewModel.districts.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Error -> {
                        Log.d(TAG, "getAllCityDistrict:  ${state.message}")
                    }

                    is UiState.Loading -> {
                        Log.d(TAG, "getAllCityDistrict:  Loading")
                    }

                    is UiState.Success -> {
                        listDistric.clear()
                        listDistric.addAll(state.data)
                        itemDistricAdapter = ItemDistricAdapter(requireContext(), listDistric)
                        binding.spnAddAddressDistrict.adapter = itemDistricAdapter
                        Log.d(TAG, "getAllCityDistrict:  ${state.data}")
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}