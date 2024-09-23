package com.duongnd.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.text.NumberFormat
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
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.Locale

class HomeAdapter(private val productList: List<DataProduct>, private val context: Context) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {


    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNameProduct: TextView = itemView.findViewById(R.id.txt_name_product)
        val txtPriceProduct: TextView = itemView.findViewById(R.id.txt_price_product)
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        val progressBarProduct: ProgressBar = itemView.findViewById(R.id.progressBarProduct)
        val btn_saveProduct: ImageButton = itemView.findViewById(R.id.btn_heart_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = View.inflate(context, R.layout.item_layout_product, null)
        return HomeViewHolder(view)
    }

    override fun getItemCount() = productList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = productList[position]

        holder.txtNameProduct.text = product.name_product


        val formatPrice = product.price.toString().replace("\\D+".toRegex(), "")
            .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")

        val locale = Locale("vi", "VN")
        val numberFormat = NumberFormat.getInstance(locale)
        val formattedPrice = numberFormat.format(product.price)
        holder.txtPriceProduct.text = "$formattedPrice vnđ"

        val imgUrl = product.imageUrls

        imgUrl.forEach {
            Glide.with(context)
                .load(it.secure_url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBarProduct.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBarProduct.visibility = View.GONE
                        return false
                    }

                })
                .into(holder.imgProduct)
        }


        holder.btn_saveProduct.setOnClickListener {
            clickToSaved?.invoke(it, productList[position])
        }

        holder.itemView.setOnClickListener {
            clickToDetail?.invoke(productList[position])
        }

    }

    var clickToSaved: ((itemView: View, product: DataProduct) -> Unit)? = null
    var clickToDetail: ((DataProduct) -> Unit)? = null

}