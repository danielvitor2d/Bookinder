package com.mobile.bookinder.screens.other_profile

import com.mobile.bookinder.common.model.Book

interface CardBookEvent {
  fun showCardBook(book: Book, position: Int)
}