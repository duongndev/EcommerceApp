package com.duongnd.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.NumberFormat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
            Picasso.get().load(it.secure_url).into(holder.imgProduct, object : Callback {
                override fun onSuccess() {
                    holder.progressBarProduct.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    holder.progressBarProduct.visibility = View.GONE
                }
            })
        }


        holder.btn_saveProduct.setOnClickListener {
            clickToSaved?.invoke(it)
        }

        holder.itemView.setOnClickListener {
            clickToDetail?.invoke(productList[position])
        }

    }

    var clickToSaved: ((itemView: View) -> Unit)? = null
    var clickToDetail: ((DataProduct) -> Unit)? = null

}