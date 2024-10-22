package com.duongnd.ecommerceapp.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class FormatPrice {
    companion object {
        fun formatPriceVN(price: Int): String {
            val locale = Locale("vi", "VN")
            val number = BigDecimal(price)
            val numberFormat = NumberFormat.getInstance(locale)
            return "${numberFormat.format(number)} vnđ"
        }

        fun formatPriceDollar(price: Int): String {
            val number = BigDecimal(price)
            val locale = Locale("en", "US")
            val numberFormat = NumberFormat.getInstance(locale)
            return "$ ${numberFormat.format(number)}"
        }
    }
}