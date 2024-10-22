package com.duongnd.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipDistricts

class ItemDistricAdapter(private val context: Context, private val items: List<DataGoShipDistricts>) :
    BaseAdapter() {
    override fun getCount() = items.size
    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(
        position: Int, convertView: View?, parent: ViewGroup?
    ): View? {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_spinner_distric, parent, false)
        val item = getItem(position) as DataGoShipDistricts
        val text = view.findViewById<TextView>(R.id.spinner_text)
        text.text = item.name
        return view
    }

}