package com.mobile.bookinder.common.interfaces

import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like

interface FeedCardBookEvent {
  fun showCardBook(book: Book, position: Int)
  fun likeBook(book: Book, position: Int)
  fun deslikeBook(book: Book, position: Int, like: Like?)

}