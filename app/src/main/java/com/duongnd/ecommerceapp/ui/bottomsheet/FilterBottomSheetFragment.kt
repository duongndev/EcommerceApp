package com.duongnd.ecommerceapp.ui.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.databinding.FragmentFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.NumberFormat
import java.util.Currency

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.addChip(requireContext(), "Relevance")
        binding.chipGroup.addChip(requireContext(), "Price: Low - High")
        binding.chipGroup.addChip(requireContext(), "Price: High - Low")
        binding.chipGroup.addChip(requireContext(), "Newest")
        binding.chipGroup.addChip(requireContext(), "Rating: Low - High")
        binding.chipGroup.addChip(requireContext(), "Rating: High - Low")
        binding.chipGroup.addChip(requireContext(), "Name: A - Z")
        binding.chipGroup.addChip(requireContext(), "Name: Z - A")
        val firstChip: Chip = binding.chipGroup.getChildAt(0) as Chip
        firstChip.isChecked = true


        binding.sliderPrice.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("VND")
            format.format(value.toDouble())
        }

        binding.imgCloseFilter.setOnClickListener {
            dismiss()
        }
    }




    private fun ChipGroup.addChip(context: Context, label: String) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            addView(this)
            chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
            setTextColor(ContextCompat.getColor(context, R.color.black))
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

}