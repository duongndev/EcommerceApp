package com.duongnd.ecommerceapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.adapter.ReviewAdapter
import com.duongnd.ecommerceapp.data.model.product.Review
import com.duongnd.ecommerceapp.databinding.FragmentReviewBinding
import com.duongnd.ecommerceapp.ui.MainActivity

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val TAG = "ReviewFragment"

    private lateinit var reviewAdapter: ReviewAdapter
    private var reviewList = ArrayList<Review>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        reviewAdapter = ReviewAdapter(requireContext(), reviewList)
        binding.recyclerViewReview.setHasFixedSize(true)
        binding.recyclerViewReview.adapter = reviewAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showBottomNavigation(false)

        arguments?.let {
            reviewList = it.getParcelableArrayList<Review>("reviewList") as ArrayList<Review>
            reviewAdapter.notifyDataSetChanged()
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}