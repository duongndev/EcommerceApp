package com.duongnd.ecommerceapp.utils

class PagedList<T>(private val items: List<T>, private val pageSize: Int) {
    private var currentPage = 0

    fun getNextPage(): List<T> {
        val start = currentPage * pageSize
        val end = minOf((currentPage + 1) * pageSize, items.size)
        currentPage++
        return items.subList(start, end)
    }

    fun hasMorePages(): Boolean {
        return currentPage * pageSize < items.size
    }
}