package com.duongnd.ecommerceapp.adapter

import android.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipDistricts

class DistrictsAdapter(
    context: Context,
    private val districts: MutableList<DataGoShipDistricts>
) : ArrayAdapter<DataGoShipDistricts>(context, R.layout.simple_spinner_item, districts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val cityNameTextView = view.findViewById<TextView>(R.id.text1)
        cityNameTextView.text = districts[position].name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = super.getDropDownView(position, convertView, parent)
        val cityNameTextView = view.findViewById<TextView>(R.id.text1)
        cityNameTextView.text = districts[position].name
        return view

    }
}