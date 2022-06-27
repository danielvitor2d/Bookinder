package com.mobile.bookinder.common.interfaces

import com.mobile.bookinder.common.model.Book

interface CardBookEvent {
    fun removeCardBook(book: Book, position: Int)
    fun showCardBook(book: Book, position: Int)
}