package com.mobile.bookinder.common.`interface`

import com.mobile.bookinder.common.model.Book

interface CardBookEvent {
    fun removeCardBook(book: Book, position: Int)
    fun showCardBook(book: Book, position: Int)
}