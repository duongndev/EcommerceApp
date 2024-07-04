package com.duongnd.ecommerceapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CheckoutAdapter(
    private val itemsCartList: MutableList<ItemCart>,
    private val context: Context
) : RecyclerView.Adapter<CheckoutAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProductCart: ImageView = itemView.findViewById(R.id.imgProductCart)
        val progressBarProductCart: ProgressBar = itemView.findViewById(R.id.progressBarProductCart)
        val txtNameProductCart: TextView = itemView.findViewById(R.id.txt_name_cart)
        val txtPriceProductCart: TextView = itemView.findViewById(R.id.txt_price_cart)
        val quantityProductCart: TextView = itemView.findViewById(R.id.txt_quantity_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = View.inflate(context, R.layout.item_layout_checkout, null)
        return CartViewHolder(view)
    }

    override fun getItemCount() = itemsCartList.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val itemCart = itemsCartList[position]

        val formatPrice = itemCart.price.toString().replace("\\D+".toRegex(), "")
            .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")
        holder.txtNameProductCart.text = itemCart.name
        holder.txtPriceProductCart.text = "$formatPrice vnđ"
        holder.quantityProductCart.text = "x${itemCart.quantity}"
        Picasso.get()
            .load(itemCart.image)
            .into(holder.imgProductCart, object : Callback {
                override fun onSuccess() {
                    holder.progressBarProductCart.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    holder.progressBarProductCart.visibility = View.GONE
                }
            })

    }


}