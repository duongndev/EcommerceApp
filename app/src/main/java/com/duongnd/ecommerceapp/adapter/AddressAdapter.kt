package com.duongnd.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.address.AddressItem
import com.duongnd.ecommerceapp.data.model.cart.Cart
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.data.model.product.DataProduct
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class AddressAdapter(
    private val itemsAddressList: MutableList<AddressItem>,
    private val context: Context
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var selectedPosition = -1

    class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitleAddress: TextView = itemView.findViewById(R.id.txt_item_title_address)
        val txtAddressLine: TextView = itemView.findViewById(R.id.txt_item_address_line)
        val txtAddressPhone: TextView = itemView.findViewById(R.id.txt_item_address_phone)
        val radioButton: RadioButton = itemView.findViewById(R.id.radio_button_selected_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = View.inflate(context, R.layout.item_layout_address, null)
        return AddressViewHolder(view)
    }

    override fun getItemCount() = itemsAddressList.size
    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: AddressViewHolder,  position: Int) {
        val address = itemsAddressList[position]
        holder.txtTitleAddress.text = address.title
        holder.txtAddressLine.text = address.addressLine
        holder.txtAddressPhone.text = address.phoneNumber

        holder.radioButton.isChecked = position == selectedPosition
        holder.radioButton.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    fun getSelectedItem(): AddressItem? {
        return if (selectedPosition!= -1) itemsAddressList[selectedPosition] else null
    }


}