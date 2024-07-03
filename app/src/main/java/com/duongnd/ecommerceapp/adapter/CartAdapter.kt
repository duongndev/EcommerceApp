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

class CartAdapter(
    private val itemsCartList: MutableList<ItemCart>,
    private val context: Context
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProductCart: ImageView = itemView.findViewById(R.id.imgProductCart)
        val progressBarProductCart: ProgressBar = itemView.findViewById(R.id.progressBarProductCart)
        val txtNameProductCart: TextView = itemView.findViewById(R.id.txt_name_cart)
        val txtPriceProductCart: TextView = itemView.findViewById(R.id.txt_price_cart)
        val minusProductCart: ImageView = itemView.findViewById(R.id.img_minus_cart)
        val quantityProductCart: TextView = itemView.findViewById(R.id.txt_quantity_cart)
        val plusProductCart: ImageView = itemView.findViewById(R.id.img_plus_cart)
        val imgDeleteCart: ImageView = itemView.findViewById(R.id.img_delete_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = View.inflate(context, R.layout.item_layout_cart, null)
        return CartViewHolder(view)
    }

    override fun getItemCount() = itemsCartList.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val itemCart = itemsCartList[position]

        val formatPrice = itemCart.price.toString().replace("\\D+".toRegex(), "")
            .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")
        holder.txtNameProductCart.text = itemCart.name
        holder.txtPriceProductCart.text = "$formatPrice vnđ"
        holder.quantityProductCart.text = itemCart.quantity.toString()
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

        holder.plusProductCart.setOnClickListener {
           incrementQuantity?.invoke(itemsCartList[position])
        }

        holder.minusProductCart.setOnClickListener {
            decrementQuantity?.invoke(itemsCartList[position])
        }

        holder.imgDeleteCart.setOnClickListener {
            deleteCart?.invoke(itemsCartList[position])
        }
    }
    var incrementQuantity:((ItemCart) -> Unit)? = null
    var decrementQuantity:((ItemCart) -> Unit)? = null
    var deleteCart:((ItemCart) -> Unit)? = null

    fun deleteItem(position: Int) {
        itemsCartList.removeAt(position)
        notifyItemRemoved(position)
    }


}