package com.duongnd.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.duongnd.ecommerceapp.data.model.wishlist.WishlistItem

class WishlistAdapter(private val productList: List<WishlistItem>, private val context: Context) :
    RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNameProductWishlist: TextView = itemView.findViewById(R.id.txt_name_product)
        val txtPriceProductWishlist: TextView = itemView.findViewById(R.id.txt_price_product)
        val imgProductWishlist: ImageView = itemView.findViewById(R.id.imgProduct)
        val progressBarProductWishlist: ProgressBar = itemView.findViewById(R.id.progressBarProduct)
        val btn_saveProductWishlist: ImageButton = itemView.findViewById(R.id.btn_heart_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = View.inflate(context, R.layout.item_layout_product, null)
        return WishlistViewHolder(view)
    }

    override fun getItemCount() = productList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val product = productList[position]
        product.productId.let {
            holder.txtNameProductWishlist.text = it.name_product
            holder.txtPriceProductWishlist.text = "${it.price}"
            holder.btn_saveProductWishlist.setOnClickListener {
                clickToSaved?.invoke(it, productList[position])
            }
        }

        product.productId.imageUrls.let {
            Glide.with(context)
                .load(it[0].secure_url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBarProductWishlist.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBarProductWishlist.visibility = View.GONE
                        return false
                    }

                })
                .into(holder.imgProductWishlist)
    }


}

var clickToSaved: ((itemView: View, product: WishlistItem) -> Unit)? = null
var clickToDetail: ((DataProduct) -> Unit)? = null

}