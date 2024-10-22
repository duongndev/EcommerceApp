package com.duongnd.ecommerceapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity

class CityAdapter(
    context: Context,
    private val cities: MutableList<DataGoShipCity>
) : ArrayAdapter<DataGoShipCity>(context, android.R.layout.simple_spinner_item, cities) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val cityNameTextView = view.findViewById<TextView>(android.R.id.text1)
        cityNameTextView.text = cities[position].name  // Hiển thị tên thành phố
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = super.getDropDownView(position, convertView, parent)
        val cityNameTextView = view.findViewById<TextView>(android.R.id.text1)
        cityNameTextView.text = cities[position].name  // Hiển thị tên thành phố trong danh sách dropdown
        return view

    }
}