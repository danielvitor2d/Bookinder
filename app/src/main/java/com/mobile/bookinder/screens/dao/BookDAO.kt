package com.mobile.bookinder.screens.dao

import com.mobile.bookinder.common.Book

class BookDAO {

  fun insert(new_book: Book): Boolean{
    if(new_book.title.isEmpty()){
      return false
    }
    BookDAO.bookList.add(new_book)
    return true
  }

  fun newId(): Int {
    return BookDAO.bookList.size+1
  }

  companion object {
    val bookList: MutableList<Book> = mutableListOf()
  }
}