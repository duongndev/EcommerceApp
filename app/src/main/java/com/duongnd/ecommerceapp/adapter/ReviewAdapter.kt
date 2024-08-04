package com.duongnd.ecommerceapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.product.Review

class ReviewAdapter(
    private val context: Context,
    private val reviewList: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNameUser: TextView = itemView.findViewById(R.id.txt_name_user)
        val txtDateReview: TextView = itemView.findViewById(R.id.txt_date_review)
        val txtContentReview: TextView = itemView.findViewById(R.id.txt_content_review)
        val ratingBarReview: RatingBar = itemView.findViewById(R.id.ratingBar_review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount() = reviewList.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.txtDateReview.text = review.createdAt
        holder.txtContentReview.text = review.comment
        holder.ratingBarReview.rating = review.rating as Float
    }
}